package auth.storage

internal class OAuthStorageKeys(
    provider: String,
) {
    private val oauthPrefix = "$provider.oauth"
    private val profilePrefix = "$provider.profile"

    val accessToken = "$oauthPrefix.access_token"
    val refreshToken = "$oauthPrefix.refresh_token"
    val tokenType = "$oauthPrefix.token_type"
    val expiresInSeconds = "$oauthPrefix.expires_in_seconds"
    val codeVerifier = "$oauthPrefix.code_verifier"

    val profileDisplayName = "$profilePrefix.display_name"
    val profileAvatarUrl = "$profilePrefix.avatar_url"
}
