package cn.mcarl.miars.core.impl.lunarclient.object;

/**
 * Lunar client has hidden StaffModule that can be
 * sent to a user at any given point.
 * <p>
 * Currently only XRAY is implemented but there are
 * future plans to expand this to possibly NAME_TAGS,
 * and there is still discussion of whether BUNNY_HOP
 * will make a return.
 * <p>
 * NOTE: Lunar Client will NOT check if the user is
 * actually staff, if the user is given any StaffModule
 * by the server it is assumed that the server knows what
 * it is doing and will not check permissions or other.
 */
public enum StaffModule {

    XRAY,
    @Deprecated
    NAME_TAGS,
    @Deprecated
    BUNNY_HOP

}
