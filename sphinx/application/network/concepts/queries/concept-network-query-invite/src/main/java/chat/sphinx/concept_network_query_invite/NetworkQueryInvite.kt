package chat.sphinx.concept_network_query_invite

import chat.sphinx.concept_network_query_invite.model.HubLowestNodePriceResponse
import chat.sphinx.concept_network_query_invite.model.HubRedeemInviteResponse
import chat.sphinx.concept_network_query_invite.model.InviteDto
import chat.sphinx.concept_network_query_invite.model.RedeemInviteResponseDto
import chat.sphinx.kotlin_response.LoadResponse
import chat.sphinx.kotlin_response.ResponseError
import chat.sphinx.wrapper_relay.RelayUrl
import kotlinx.coroutines.flow.Flow

abstract class NetworkQueryInvite {

    ///////////
    /// GET ///
    ///////////

    ///////////
    /// PUT ///
    ///////////

    ////////////
    /// POST ///
    ////////////
//    app.post('/invites', invites.createInvite)
//    app.post('/invites/:invite_string/pay', invites.payInvite)
//    app.post('/invites/finish', invites.finishInvite)

    abstract fun getLowestNodePrice(): Flow<LoadResponse<HubLowestNodePriceResponse, ResponseError>>

    // TODO: Return RedeemInviteResponse
    abstract fun redeemInvite(
        inviteString: String
    ): Flow<LoadResponse<HubRedeemInviteResponse, ResponseError>>

    abstract fun finishInvite(
        inviteString: String
    ): Flow<LoadResponse<RedeemInviteResponseDto, ResponseError>>

    abstract fun payInvite(
        inviteString: String
    ): Flow<LoadResponse<InviteDto, ResponseError>>

    //////////////
    /// DELETE ///
    //////////////
}
