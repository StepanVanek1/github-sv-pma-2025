package com.example.myapp013aeducationgame.database.word

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapp013aeducationgame.database.user.Word

@Dao
interface WordDao {
    @Insert
    suspend fun insertAll(words: List<Word>)

    @Query("SELECT * FROM word ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord(): Word?

    @Query("SELECT * FROM word")
    suspend fun getAllWords(): List<Word?>

    @Query("SELECT * FROM word WHERE id != :excludeId ORDER BY RANDOM() LIMIT 2")
    suspend fun getRandomWordsExcluding(excludeId: Long): List<Word>

    @Query(
        """
    INSERT INTO word (czech, `foreign`) VALUES 
    ('kočka', 'cat'),
    ('pes', 'dog'),
    ('dům', 'house'),
    ('strom', 'tree'),
    ('auto', 'car'),
    ('kniha', 'book'),
    ('slunce', 'sun'),
    ('měsíc', 'moon'),
    ('voda', 'water'),
    ('jablko', 'apple'),
    ('ryba', 'fish'),
    ('pták', 'bird'),
    ('kolo', 'bike'),
    ('město', 'city'),
    ('řeka', 'river'),
    ('hora', 'mountain'),
    ('zahrada', 'garden'),
    ('hory', 'mountains'),
    ('jezero', 'lake'),
    ('les', 'forest')
"""
    )
    suspend fun insertInitialWords()
}