package Model

class User {

    internal lateinit var user_name: String
    internal lateinit var user_id: String
    internal lateinit var user_email: String
    internal lateinit var friends: List<ListItemFriend>
    internal lateinit var participate_room: List<ListItemRoom>
    internal var connecting = false

}