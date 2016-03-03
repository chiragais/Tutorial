package pokerserver.players;

import java.util.ArrayList;

public class PlayersManager {

	public ArrayList<PlayerBean> roomPlayersList; // all player container list

	
	public PlayersManager() {
		
		 roomPlayersList = new ArrayList<>();
	}

	public void addNewPlayerInRoom(PlayerBean player) {
		this.roomPlayersList.add(player);
	}

	public ArrayList<PlayerBean> getAllAvailablePlayers(){
		return roomPlayersList;
	}
	public PlayerBean getPlayerByName(String name) {
		for (PlayerBean player : roomPlayersList) {
			if (player.getPlayeName().equals(name)) {
				return player;
			}
		}
		return null;
	}
	
	public void setAllAvailablePlayers(ArrayList<PlayerBean> playerList){
		roomPlayersList.clear();
		roomPlayersList.addAll(playerList);
		
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
