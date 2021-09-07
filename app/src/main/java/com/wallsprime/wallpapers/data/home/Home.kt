package com.wallsprime.wallpapers.data.home

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "home")
data class Home(
    @PrimaryKey val homeId: String


)