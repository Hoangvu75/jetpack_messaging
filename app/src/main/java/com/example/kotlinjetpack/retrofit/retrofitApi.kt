import com.example.kotlinjetpack.model.*
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {
    @POST("/auth/login")
    suspend fun login(@Body body: HashMap<String, String>): Response<LoginResponse>

    @POST("/auth/register")
    suspend fun register(@Body body: HashMap<String, String>): Response<RegisterResponse>

    @POST("/auth/add-profile")
    suspend fun addProfile(@Body body: HashMap<String, String>): Response<AddProfileResponse>

    @GET("/auth/get-profile")
    suspend fun getProfile(@Header("Authorization") authHeader: String): Response<GetProfileResponse>

    @GET("/contact/get-contact")
    suspend fun getContact(@Header("Authorization") authHeader: String): Response<GetContactResponse>

    @POST("/contact/create-contact")
    suspend fun createContact(@Header("Authorization") authHeader: String): Response<Void>

    @POST("/contact/add-contact-item/{phone}")
    suspend fun addContact(
        @Header("Authorization") authHeader: String,
        @Path("phone") phone: String
    ): Response<AddContactResponse>

    @POST("/contact/delete-contact-item/{phone}")
    suspend fun deleteContact(
        @Header("Authorization") authHeader: String,
        @Path("phone") phone: String
    ): Response<DeleteContactResponse>

    @GET("/chat/get-chat-list")
    suspend fun getChatList(@Header("Authorization") authHeader: String): Response<GetChatListResponse>

    @GET("/chat/get-chat/{chatId}")
    suspend fun getChat(
        @Path("chatId") chatId: String
    ): Response<GetChatResponse>

    @POST("/chat/add-chat")
    suspend fun addChat(@Body addChatRequestBody: AddChatRequestBody): Response<Unit>

    @POST("/chat/create-chat")
    suspend fun createChat(@Body createChatRequestBody: CreateChatRequestBody): Response<Unit>
}

data class AddChatRequestBody(
    val user_1: User,
    val user_2: User,
    val sender: String,
    val content: String
)

data class CreateChatRequestBody(
    val user_1: CreateChatUser,
    val user_2: CreateChatUser
)

data class CreateChatUser(
    val phone: String?,
    val name: String?
)