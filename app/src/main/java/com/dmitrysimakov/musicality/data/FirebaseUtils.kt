package com.dmitrysimakov.musicality.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

val firestore = FirebaseFirestore.getInstance()

fun generateId() = firestore.collection("path").document().id

val songsCollection = firestore.collection("songs")


val storage = FirebaseStorage.getInstance().reference

val songsStorage = storage.child("songs")
val artsStorage = storage.child("arts")