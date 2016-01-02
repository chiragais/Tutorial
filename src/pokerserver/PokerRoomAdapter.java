package pokerserver;

import com.shephertz.app42.server.idomain.BaseTurnRoomAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.ITurnBasedRoom;
import com.shephertz.app42.server.idomain.IUser;
import com.shephertz.app42.server.idomain.IZone;
/**
 * 
 * @author Chirag
 */
public class PokerRoomAdapter extends BaseTurnRoomAdaptor {

	private IZone izone;
	private ITurnBasedRoom gameRoom;
	private byte GAME_STATUS;

	public PokerRoomAdapter(IZone izone, ITurnBasedRoom room) {
		this.izone = izone;
		this.gameRoom = room;
		GAME_STATUS = CardsConstants.STOPPED;
		System.out.println();
		System.out.print("Room : PokerRoomAdapter : Room : " + room.getName());
	}

	@Override
	public void onTimerTick(long time) {
		/*
		 * A game when room full or we can say max users are equals to joined
		 * users
		 */
		if (GAME_STATUS != CardsConstants.RUNNING) {
			System.out.println();
			System.out.print("Room : onTimerTick : "
					+ gameRoom.getJoinedUsers().size());
		}
//		if (GAME_STATUS == CardsConstants.STOPPED
//				&& gameRoom.getJoinedUsers().size() >= 2) {
		if (GAME_STATUS == CardsConstants.STOPPED) {
			GAME_STATUS = CardsConstants.RUNNING;
			gameRoom.startGame(CardsConstants.SERVER_NAME);
			System.out.println();
			System.out.print("Room : onTimerTick : Start Game : ");
		} else if (GAME_STATUS == CardsConstants.RESUMED) {
			GAME_STATUS = CardsConstants.RUNNING;
			gameRoom.startGame(CardsConstants.SERVER_NAME);
		}

	}

	/**
	 * Invoked when a move request is received from the client whose turn it is.
	 * 
	 * By default, the sender will be sent back a success response, the turn
	 * user will be updated to the next user in order of joining and a move
	 * notification will be sent to all the subscribers of the room.
	 * 
	 * @param sender
	 *            the user who has sent the move
	 * @param moveData
	 *            the move data sent by the user
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleMoveRequest(IUser sender, String moveData,
			HandlingResult result) {
		System.out.println();
		System.out.print("Room : handleMoveRequest : Sender User : "
				+ sender.getName() + " : Data : " + moveData);

	}

	/**
	 * Invoked when a start game request is received from a client when the room
	 * is in stopped state.
	 * 
	 * By default a success response will be sent back to the client, the game
	 * state will be updated and game started notification is sent to all the
	 * subscribers of the room.
	 * 
	 * @param sender
	 *            the user who has sent the request.
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleStartGameRequest(IUser sender, HandlingResult result) {
		System.out.println();
		System.out.print("Room : handleStartGameRequest : Sender User : "
				+ sender.getName());
	}

	/**
	 * Invoked when a stop game request is received from a client when the room
	 * is in started state.
	 * 
	 * By default a success response will be sent back to the client, the game
	 * state will be updated and game stopped notification is sent to all the
	 * subscribers of the room.
	 * 
	 * @param sender
	 *            the user who has sent the request.
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleStopGameRequest(IUser sender, HandlingResult result) {
		System.out.println();
		System.out.print("Room : handleStopGameRequest : Sender User : "
				+ sender.getName());
	}

	/**
	 * Invoked when the timer expires for the current turn user.
	 * 
	 * By default, the turn user will be updated to the next user in order of
	 * joining and a move notification with empty data will be sent to all the
	 * subscribers of the room.
	 * 
	 * @param turn
	 *            the current turn user whose turn has expired.
	 * @param result
	 *            use this to override the default behavior
	 */
	public void onTurnExpired(IUser turn, HandlingResult result) {
		System.out.println();
		System.out
				.print("Room : onTurnExpired : Turn User : " + turn.getName());
	}

	/**
	 * Invoked when a user leaves the turn based room.
	 * 
	 * By default, the turn user will be updated to the next user in order of
	 * joining if the user who is leaving was the current turn user and a move
	 * notification with empty data will be sent to all the subscribers of the
	 * room.
	 * 
	 * @param user
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleUserLeavingTurnRoom(IUser user, HandlingResult result) {
		System.out.println();
		System.out.print("Room : handleUserLeavingTurnRoom :  User : "
				+ user.getName());
	}

	/**
	 * Invoked when a chat request is received from the client in the room.
	 * 
	 * By default this will trigger a success response back to the client and
	 * will broadcast a notification message to all the subscribers of the room.
	 * 
	 * 
	 * @param sender
	 *            the user who has sent the request
	 * @param message
	 *            the message that was sent
	 * @param result
	 *            use this to override the default behavior
	 */
	public void handleChatRequest(IUser sender, String message,
			HandlingResult result) {
		System.out.println();
		System.out.print("Room : handleChatRequest :  User : "
				+ sender.getName() + " : Message : " + message);
	}

	 /**
     * Invoked when a join request is received by the room and the number of
     * joined users is less than the maxUsers allowed.
     * 
     * By default this will result in a success response sent back the user, 
     * the user will be added to the list of joined users of the room and
     * a user joined room notification will be sent back to all the subscribed 
     * users of the room.
     * 
     * @param user the user who has sent the request
     * @param result use this to override the default behavior
     */
	public void handleUserJoinRequest(IUser user, HandlingResult result){
		System.out.println();
		System.out.print("Room : handleUserJoinRequest :  User : "
				+ user.getName() );
		gameRoom.BroadcastChat(CardsConstants.SERVER_NAME, "New User Joined");

    }
	public void onUserPaused(IUser user) {

	}

	public void onUserResume(IUser user) {

	}
}
