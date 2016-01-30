package pokerserver.players;

import java.util.ArrayList;

public class PlayersManager {

	public ArrayList<Player> roomPlayersList = new ArrayList<>(); // all player container list

	public PlayersManager() {

	}

	public void addNewPlayerInRoom(Player player) {
		this.roomPlayersList.add(player);
	}

	public ArrayList<Player> getAllAvailablePlayers(){
		return roomPlayersList;
	}
	public int getTotalRoomPlayers() {
		return roomPlayersList.size();
	}

	public Player getPlayer(int plrId) {
		return roomPlayersList.get(plrId);
	}
	
	public void removePlayerFromRoom(Player player) {
		this.roomPlayersList.remove(player);
	}
	public void removeAllPlayers(){
		this.roomPlayersList.clear();
	}
}
