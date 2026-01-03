import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

data class MapAccessResponse(
    val allowed: Boolean,
    val source: String? = null,
    val expiresOn: String? = null,
    val message: String? = null
)

interface MapAccessApi {
    @GET("map/access-check")
    suspend fun checkMapAccess(
        @Header("Authorization") token: String
    ): Response<MapAccessResponse>
}
