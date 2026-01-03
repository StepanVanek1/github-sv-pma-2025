import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quizgame.database.game.Game
import com.example.quizgame.database.game.GameDao
import com.example.quizgame.database.question.Question
import com.example.quizgame.database.question.QuestionDao
import com.example.quizgame.database.quiz.QuizDao
import com.example.quizgame.database.user.User
import com.example.quizgame.database.user.UserDao

@Database(entities = [Question::class, User::class, Game::class, Quiz::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun userDao(): UserDao
    abstract fun gameDao(): GameDao
    abstract fun quizDao(): QuizDao
}