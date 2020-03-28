# Room

Room is one of the Android architecture components which are a bunch of different libraries which help create more maintainable apps. Room provides a layer of abstraction and bridges java objects to SQLite database.

Previous, creating apps without Room was complicated because you would have to write all the database code manually. This often invovled creating helper classes which could get very large and were not very robust or maintainable. Room makes use of annotations and does most of the hard work for you.

To make use of Room in a project, you will need to import it using Gradle. There are many support imports using kapt and a different compiler that can also be used.

## Room with MVVM

With Room, data classes can be turned into Entities which represent a table in a SQLite database (using @Enity). Data Access Objects (DAOs) are used to communicate with SQLite database through annotations. This can then interact with a ViewModel which has the job of holiding and preparing all the data for the frontend. The advantage of this is that the Activities/Fragments don't know about the data directly and therefore only need to worry about final output and reporting user interactions back to the ViewModel. This also removes the the possiblility of Activity Leaks.

The ViewModel acts as a gateway between the UI and DAO. ViewModel can interact directly with DAO, however, it is recommended to use a repository allowing for data sources to be changed more easily. As an example, you could change a database for a web serivce. 

With this setup, you have the Activity/Fragment as the View, the ViewModel, then the repository and data source as the Model. LiveData is a wrapper than can hold any type of data including lists and can be observed by the UI controller. This means whenever the data in the LiveData object changes the observer automatically gets notified with the new data and can refresh the UI in reaction. Another great thing about LiveData is that it is lifecycle aware meaning it knows when the Activity/Fragment that observes it is in the background and automatically stops updating it until it comes back into the foreground. This means that you dont manually stop and resume observation in Activities/Fragments. It also cleans up any references when the lifecycle is destroyed.

## Entity

@Entity is a Room annotation which goes above a data class. At compile time it generates all the necessary code to create an SQLite table. Room uses annotations to hide all the boilerplate code (instead of having to write it all in a helper class). The Entity annotation take a table name: @Entity(tableName = "user_table"). Room automatically creates columns for members of the data class. If you want to overwrite the variable name, add the annoation @ColumnInfo(name = "new_name").

As with any SQL table, each Entity needs a primary key. The suggested way to handle this is having a member variable called id as an int or long with the annotation: @PrimaryKey(autoGenerate = true). If using auto generate, put the id in the body of the dataclass, not in the constructor as you will not want to pass in an id when creating instances of the data class.

## Data Access Object (DAO)

DAOs are used to communicate with SQLite through annotations. DAOs can be implemented either using Interfaces or Abstract classes. Interfaces are most commonly used. Generally, one DAO is made for one Entity but this can depend on the use case. DAOs are defined using the @DAO annotation above the declaration of the Interface.

DAO interfaces contain methods for all the operations required against the table/entity. An example of this would be: @Insert fun insert(User user). Other important annoatations include @Update and @Delete. CLicking through to the documentation will show how different data can be handled such as multiple arguments or lists.

@Query is the other import annoation. For example, to delete all, you would do the following: @Query("DELETE FROM table_name") fun deleteAll(). For get all: @Query("SELECT * FROM table_name") fun getAll(): List<String>. In certain situations, you might want to use @Query instead of @Delete: @Query("DELETE FROM table WHERE id = :id") fun deleteById()id: Int. The important thing to note is that with Room and annotations, with the quotations (""), if you make a typo, it will be underlined in red reducing errors. 

Room can return LiveData. This is delcared with the return object: LiveData<List<Object>>. This allows for the object to be observed. This means as soon as there are any changes in the SQLite table, the object will be updated and notified. Room takes care of all the updating.

## Database Class

The database class brings the Entity and DAO together and creates an instance of the database itself. This class extends the RoomDatabase class and needs to be abstract with the annoation @Database and should include the entities: @Database(entities = {User.class}, version = 1)

The class needs to contain a static variable/companion object of the class itself called instance. This allows for a singleton to be implemented meaning only one instance of the database can exist. The Kotlin annoation @Volatile is used meaning the variable is available for other threads immediately meaning exactly one instance across a multithread applicaiton will be used. The instance is implemented using the following code:

companion object {
    @Volatile
    private var INSTANCE: QuoteDatabase? = null

    fun getDatabase(context: Context): QuoteDatabase {
        val tempInstance = INSTANCE
        if (tempInstance != null) {
            return tempInstance
        }

        synchronized(this){
            val instance = Room.databaseBuilder(context.applicationContext, QuoteDatabase::class.java, "quotivator_database").fallbackToDestructiveMigration.build()
            INSTANCE = instance
            return instance
        }
    }
}

When incrementing the version number of the database, you have to tell Room how to migrate the schema. If you don't do this, an illegal state exception will occur. Using FallbackToDestructiveMigration avoids this by deleting the pervious version and all the data.

DAO are declared as abstract member variables of the class like: abstract fun userDao: UserDao()

## Repository Class

The repository class allows for communication between the ViewModel and the Model (database). The class allows for another layer of abstraction to be achieved. The repository class also allows for different data streams such as databases or web services to be replaced without any change to the front end code.

The class should contain an instance of the DAO in its constructor. Using the DAO, suspended functions should be made accessing all the DAOs methods (insert, update...). Get all is the exception to this as this can just be a member variable of the class as this is as a get

class UserRepository(private val userDao: userDao) {
    val allUsers: LiveData<List<User>> = userDao.getAllUser()

    suspend fun insert(user: User) {
        userDao.insert(user)
    }
    ...
}

The suspend function is a way of running queries on the background thread. Room does not allow you to execute SQL functionaility on the main thread as it will freeze the app. The suspend modifier tells the compiler that this needs to be called from a coroutine or another suspended function. Coroutines need to be run inside a CoroutineScope. This could be done in a class scope (classes like ViewModel have there own scope) or runBlocking { ... } (which can be used in tests). 

## ViewModel

The ViewModel's role is to provide data to the UI and survive config changes. A ViewModel acts as a communcation center between the Repository and the UI. You can also use a ViewModel to share data between fragments. In the ViewModel, use LiveData for changable data that the UI will use or display.

ViewModels also have their own scope. In Kotlin, all coroutines run inside a CoroutineScope. A scope controls the lifetime of coroutines through its job. When you cancel the job of a scope, it cancels all coroutines started in that scope. The ViewModel is where you launch the scope.

class UserViewModel(application: Application): AndroidViewModel(application) {
    private val repository: UserRepository
    private val allUsers: LiveData<List<User>>

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        allUsers = repository.allUsers
    }

    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }
    ...
}

Note that there is a difference between AndroidViewModel and ViewModel. Use AndroidViewModel when you have the application context which has a lifecyclle that lives as long as the application does.