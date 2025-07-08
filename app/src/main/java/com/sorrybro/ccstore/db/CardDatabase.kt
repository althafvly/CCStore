package com.sorrybro.ccstore.db

import android.content.Context
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

    companion object {
        @Volatile
        private var INSTANCE: CardDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE cards ADD COLUMN bankName TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getInstance(context: Context, passphrase: String): CardDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, passphrase).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context, passphrase: String): CardDatabase {
            System.loadLibrary("sqlcipher")
            val factory = SupportOpenHelperFactory(passphrase.toByteArray())
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
