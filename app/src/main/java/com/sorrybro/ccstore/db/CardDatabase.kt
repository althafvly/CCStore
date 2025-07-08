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
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

@Database(entities = [CardEntity::class], version = Constants.DB_VERSION)
abstract class CardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    init {
        System.loadLibrary("sqlcipher")
    }

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
                val instance = try {
                    buildDatabase(context, passkey).also {
                        // Validate DB
                        it.openHelper.readableDatabase.query("SELECT count(*) FROM sqlite_master").use { c ->
                            if (c.moveToFirst()) c.getInt(0)
                        }
                    }
                } catch (_: SQLiteException) {
                    buildDatabase(context, Constants.DEFAULT_PASSPHRASE)
                }

                INSTANCE = instance
                instance
            }
        }

        private fun buildDatabase(context: Context, passphrase: String): CardDatabase {
            val factory = SupportOpenHelperFactory(passphrase.toByteArray(Charsets.UTF_8))
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
