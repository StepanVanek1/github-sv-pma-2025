import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz")
data class Quiz(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val creatorId: Long,
    val createdAt: Long
)