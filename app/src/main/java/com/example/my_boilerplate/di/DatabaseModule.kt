package com.example.my_boilerplate.di

import android.content.Context
import androidx.room.Room
//import com.example.my_boilerplate.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Room scaffold DI - the database instance is ready to inject anywhere. When you add your
 * first @Dao (see database/AppDatabase.kt for the steps), add a @Provides function here:
 *
 *   @Provides
 *   @Singleton
 *   fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

//    @Provides
//    @Singleton
//    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
//        return Room.databaseBuilder(
//            context,
//            AppDatabase::class.java,
//            "app_database"
//        )
//            .fallbackToDestructiveMigration() // fine for early development; replace with real
//            // Migration objects before shipping a release with existing user data.
//            .build()
//    }
}
