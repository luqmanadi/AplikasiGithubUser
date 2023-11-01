package com.dicoding.aplikasigithubuser.data.local.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dicoding.aplikasigithubuser.data.local.entity.FavoriteUserEntity

@Database(entities = [FavoriteUserEntity::class], version = 3, exportSchema = false)
abstract class GithubUserRoomDatabase : RoomDatabase() {

    abstract fun githubUserDao(): GithubUserDao

    companion object {
        @Volatile
        private var instance: GithubUserRoomDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): GithubUserRoomDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    GithubUserRoomDatabase::class.java, "NewUserGithub.db")
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
            }
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Hapus tabel lama jika ada
                Log.d("Migration", "Dropping old table")
                database.execSQL("DROP TABLE IF EXISTS GithubUserEntity")

                // Buat tabel baru dengan struktur yang baru
                Log.d("Migration", "Creating new table")
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS TableFavoriteUser (" +
                            "username TEXT PRIMARY KEY NOT NULL," +
                            "avatarUrl TEXT)"
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Hapus tabel lama jika ada
                Log.d("Migration", "Dropping old table")
                database.execSQL("DROP TABLE IF EXISTS TableFavoriteUser")

                // Buat tabel baru dengan struktur yang baru
                Log.d("Migration", "Creating new table")
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS NewTableFavoriteUser (" +
                            "username TEXT PRIMARY KEY NOT NULL," +
                            "avatarUrl TEXT)"
                )
            }
        }
    }
}