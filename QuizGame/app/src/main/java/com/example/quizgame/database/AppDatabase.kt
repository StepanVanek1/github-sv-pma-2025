import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quizgame.database.game.Game
import com.example.quizgame.database.game.GameDao
import com.example.quizgame.database.question.Question
import com.example.quizgame.database.question.QuestionDao
import com.example.quizgame.database.user.User
import com.example.quizgame.database.user.UserDao

@Database(entities = [Question::class, User::class, Game::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun userDao(): UserDao
    abstract fun gameDao(): GameDao
}