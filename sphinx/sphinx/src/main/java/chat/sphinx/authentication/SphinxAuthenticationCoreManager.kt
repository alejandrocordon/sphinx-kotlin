package chat.sphinx.authentication

import android.app.Application
import io.matthewnelson.android_feature_authentication_core.components.AuthenticationCoreManagerAndroid
import io.matthewnelson.android_feature_authentication_core.components.AuthenticationManagerInitializerAndroid
import io.matthewnelson.concept_coroutines.CoroutineDispatchers
import io.matthewnelson.k_openssl_common.clazzes.HashIterations
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SphinxAuthenticationCoreManager @Inject constructor(
    application: Application,
    dispatchers: CoroutineDispatchers,
    encryptionKeyHandler: SphinxEncryptionKeyHandler,
    sphinxAuthenticationCoreStorage: SphinxAuthenticationCoreStorage,
): AuthenticationCoreManagerAndroid(
    dispatchers,
    HashIterations(250_000),
    encryptionKeyHandler,
    sphinxAuthenticationCoreStorage,
    AuthenticationManagerInitializerAndroid(
        application,
        minimumUserInputLength = 6,
        maximumUserInputLength = 6
    )
) {
    override val logOutWhenApplicationIsClearedFromRecentsTray: Boolean = false
}