package com.sorrybro.ccstore.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sorrybro.ccstore.Constants
import com.sorrybro.ccstore.data.CardEntity
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [CardEntity::class], version = Constants.DB_VERSION)
abstract class CardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var INSTANCE: CardDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE cards ADD COLUMN bankName TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context, passkey: String): CardDatabase {
            return INSTANCE ?: synchronized(this) {
                val passphrase = SQLiteDatabase.getBytes(passkey.toCharArray())
                val factory = SupportFactory(passphrase)
                val instance = getDatabaseBuilder(context).openHelperFactory(factory)
                    .addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }

        fun getDatabaseBuilder(context: Context): Builder<CardDatabase> {
            return Room.databaseBuilder(
                context.applicationContext,
                CardDatabase::class.java,
                Constants.DB_NAME
            )
        }

        fun isUsingOldPassphrase(context: Context): Boolean {
            val dbFile = context.getDatabasePath(Constants.DB_NAME)
            if (!dbFile.exists()) {
                return false // DB doesn't exist at all
            }

            return try {
                val passphrase = SQLiteDatabase.getBytes(Constants.DEFAULT_PASSPHRASE.toCharArray())
                val factory = SupportFactory(passphrase)

                getDatabaseBuilder(context)
                    .openHelperFactory(factory)
                    .allowMainThreadQueries() // Safe here since we're only testing
                    .build()
                    .openHelper
                    .writableDatabase // Try to open with the known passphrase

                true
            } catch (_: Exception) {
                false
            }
        }
    }
}
