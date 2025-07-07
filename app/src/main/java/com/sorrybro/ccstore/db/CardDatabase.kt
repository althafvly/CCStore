package com.sorrybro.ccstore.db

import android.content.Context
import android.database.sqlite.SQLiteException
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
                val firstTryPassphrase =
                    SQLiteDatabase.getBytes(Constants.DEFAULT_PASSPHRASE.toCharArray())
                val secondTryPassphrase = SQLiteDatabase.getBytes(passkey.toCharArray())

                val instance = try {
                    buildDatabase(context, firstTryPassphrase)
                } catch (_: SQLiteException) {
                    buildDatabase(context, secondTryPassphrase)
                }

                INSTANCE = instance
                instance
            }
        }

        private fun buildDatabase(context: Context, passphrase: ByteArray): CardDatabase {
            val factory = SupportFactory(passphrase)
            return Room.databaseBuilder(
                context.applicationContext,
                CardDatabase::class.java,
                Constants.DB_NAME
            ).openHelperFactory(factory)
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}
