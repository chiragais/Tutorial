package pokerserver.zone;

import pokerserver.room.PokerRoomAdapter;

import com.shephertz.app42.server.idomain.BaseZoneAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.ITurnBasedRoom;
import com.shephertz.app42.server.idomain.IUser;
import com.shephertz.app42.server.idomain.IZone;
/**
 * @author Chirag
 */
public class PokerZoneAdapter extends BaseZoneAdaptor {

	private IZone izone;

	PokerZoneAdapter(IZone izone) {
		this.izone = izone;
		System.out.println();
		System.out.print("Zone : PokerZoneAdapter : "+izone.getName());
	}

	/**
	 * Invoked when a create room request is received from the client.
	 * 
	 * By default this will result in a success response sent back to the user,
	 * the room will be added to the list of rooms and a room created
	 * notification will be sent to all the subscribers of the lobby.
	 * 
	 * @param user
	 *            the user who has sent the request. Null if received from admin
	 *            dashboard
	 * @param room
	 *            the room being created
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleCreateRoomRequest(IUser user, IRoom room,
			HandlingResult result) {
		System.out.println();
		System.out.print("Zone : handleCreateRoomRequest : Owner Name : "
				+ user.getName() + " :: Room Name : " + room.getName());
		room.setAdaptor(new PokerRoomAdapter(izone, (ITurnBasedRoom)room));
	}

	/**
	 * Invoked when a delete room request is received from the client.
	 * 
	 * By default this will result in a success response sent back to the user,
	 * the room will be removed from the list of rooms and a room deleted
	 * notification will be sent to all the subscribers of the lobby.
	 * 
	 * @param user
	 *            the user who has sent the request. Null if received from admin
	 *            dashboard
	 * @param room
	 *            the room being created
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleDeleteRoomRequest(IUser user, IRoom room,
			HandlingResult result) {
		System.out.println();
		System.out.print("Zone : handleDeleteRoomRequest : Owner Name : "
				+ user.getName() + " :: Room Name : " + room.getName());
	}

	/**
	 * Invoked when connect request is received from a client.
	 * 
	 * By default the user will be added to the zone and if user with the same
	 * name exists that user will be disconnected.
	 * 
	 * @param user
	 *            the user who has sent the connect request.
	 * @param authData
	 *            the authdata passed in the connect request
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleAddUserRequest(IUser user, String authData,
			HandlingResult result) {
		System.out.println();
		System.out.print("Zone : handleAddUserRequest : New User : "
				+ user.getName());
	}

	/**
	 * Invoked when a recover request is sent by a client with in the allowance
	 * time set while first connecting.
	 * 
	 * By default the user session will be successfully resumed and a response
	 * will be sent back to the user.
	 * 
	 * @param user
	 *            the user who is attempting to recover
	 * @param authData
	 *            the authData sent by the client
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleResumeUserRequest(IUser user, String authData,
			HandlingResult result) {
		System.out.println();
		System.out.print("Zone : handleResumeUserRequest : User : "
				+ user.getName());
	}

	/**
	 * Invoked every time the Game loop timer is fired. The frequency of the
	 * tick can be modified through the AppConfig.json file. Do NOT perform
	 * blocking operations in this callback.
	 * 
	 * @param time
	 *            the difference, measured in milliseconds, between the current
	 *            time and midnight, January 1, 1970 UTC as returned by
	 *            System.currentTimeMillis()
	 */
	public void onTimerTick(long time) {
//		System.out.println();
//		System.out.print("Zone : onTimerTick : " + time);
	}

	/**
	 * Invoked when a user is removed from the list of users of the zone. This
	 * happens when the client disconnects from AppWarp.
	 * 
	 * @param user
	 *            the user being removed.
	 */
	public void onUserRemoved(IUser user) {
		System.out.println();
		System.out.print("Zone : onUserRemoved : " + user.getName());
	}

	/**
	 * Invoked when a user is paused. This happens when a client's connection
	 * breaks without receiving a disconnect request and the client has set a
	 * recovery allowance period.
	 * 
	 * @param user
	 */
	public void onUserPaused(IUser user) {
		System.out.println();
		System.out.print("Zone : onUserPaused : " + user.getName());
	}

	/**
	 * Invoked when a private chat request is received from an online user for
	 * another user who is also online. By default the server will send a
	 * success response back to the sender and a private chat notification to
	 * the receiver.
	 * 
	 * @param sender
	 * @param toUser
	 * @param result
	 */
	public void handlePrivateChatRequest(IUser sender, IUser toUser,
			HandlingResult result) {
		System.out.println();
		System.out.print("Zone : handlePrivateChatRequest : Sender : "
				+ sender.getName() + " : To User : " + toString());
	}

	/**
	 * Invoked when a room is added from the admin dashboard. This is also
	 * invoked when the server is started and the previously created admin rooms
	 * are added to the zone.
	 * 
	 * @param room
	 *            the room being added.
	 */
	public void onAdminRoomAdded(IRoom room) {
		System.out.println();
		System.out.print("Zone : onAdminRoomAdded : " + room.getName());
	}

	/**
	 * Invoked when an admin room is deleted from the dashboard.
	 * 
	 * @param room
	 *            the room being removed
	 */
	public void onAdminRoomDeleted(IRoom room) {
		System.out.println();
		System.out.print("Zone : onAdminRoomDeleted : " + room.getName());
	}

}
