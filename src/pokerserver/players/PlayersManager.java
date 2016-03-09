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
	public ArrayList<PlayerBean> getAllAactivePlayersForWinning(){
		ArrayList<PlayerBean> roomPlayersList = new ArrayList<PlayerBean>();
		for (PlayerBean player : this.roomPlayersList) {
			if (!player.isFolded()) {
				roomPlayersList.add(player);
			}
		}
		return roomPlayersList;
	}
	public ArrayList<PlayerBean> getAllAactivePlayersForTurn(){
		ArrayList<PlayerBean> roomPlayersList = new ArrayList<PlayerBean>();
		for (PlayerBean playerBean : this.roomPlayersList) {
			if (!playerBean.isAllIn())
				if (!playerBean.isFolded()) {
					roomPlayersList.add(playerBean);
			}
		}
		return roomPlayersList;
	}
	public PlayerBean getPlayerByName(String name) {
		for (PlayerBean player : roomPlayersList) {
			if (player.getPlayerName().equals(name)) {
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
	
	public PlayerBean getDealerPayer(){
		PlayerBean dealerPlayer = null;
		for(int i = 0 ; i<roomPlayersList.size();i++){
			if(i==0){
				roomPlayersList.get(i).setDealer(true);
				dealerPlayer = roomPlayersList.get(i);
			}else{
				roomPlayersList.get(i).setDealer(false);
			}
		}
		return dealerPlayer;
	}
	public PlayerBean getBigBlindPayer(){
		PlayerBean bbPlayer = null;
		for(int i = 0 ; i<roomPlayersList.size();i++){
			if(i==2){
				roomPlayersList.get(i).setBigBlind(true);
				bbPlayer = roomPlayersList.get(i);
			}else{
				roomPlayersList.get(i).setBigBlind(false);
			}
		}
		return bbPlayer;
	}
	public PlayerBean getSmallBlindPayer(){
		PlayerBean sbPlayer = null;
		for(int i = 0 ; i<roomPlayersList.size();i++){
			if(i==1){
				roomPlayersList.get(i).setSmallBlind(true);
				sbPlayer = roomPlayersList.get(i);
			}else{
				roomPlayersList.get(i).setSmallBlind(false);
			}
		}
		return sbPlayer;
	}
}
