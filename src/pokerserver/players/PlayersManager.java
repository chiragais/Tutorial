package pokerserver.players;

import java.util.ArrayList;

public class PlayersManager {

	public ArrayList<PlayerBean> roomPlayersList = new ArrayList<>(); // all player container list

	public PlayersManager() {

	}

	public void addNewPlayerInRoom(PlayerBean player) {
		this.roomPlayersList.add(player);
	}

	public ArrayList<PlayerBean> getAllAvailablePlayers(){
		return roomPlayersList;
	}
	public int getTotalRoomPlayers() {
		return roomPlayersList.size();
	}

	public PlayerBean getPlayer(int plrId) {
		return roomPlayersList.get(plrId);
	}
	
	public void removePlayerFromRoom(PlayerBean player) {
		this.roomPlayersList.remove(player);
	}
	public void removeAllPlayers(){
		this.roomPlayersList.clear();
	}
}
