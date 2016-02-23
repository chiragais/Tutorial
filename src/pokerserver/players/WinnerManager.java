package pokerserver.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pokerserver.cards.Card;

public class WinnerManager {
	
	
	
	PlayersManager playerManager;
	ArrayList<Winner> listWinners ;
	ArrayList<AllInPlayer> listAllinPotAmounts ;
	ArrayList<PlayerBean> listAllSortedRankPlayers ;
	ArrayList<PlayerBean> listAllFinalizePlayers ;
	int totalTableAmount=0;
	int remainingAmount=0;
  
   public	WinnerManager(PlayersManager playerMgr){
	  
	   this.playerManager=playerMgr;
	   listWinners= new ArrayList<Winner>();
	   listAllinPotAmounts=new ArrayList<AllInPlayer>();
	   listAllFinalizePlayers=new ArrayList<PlayerBean>();
	   listAllSortedRankPlayers=new ArrayList<PlayerBean>();
	  
   }
	
   public void addWinner(Winner winner){
		this.listWinners.add(winner);
	}
    
   public void addAllInTotalPotAmount(AllInPlayer allInPlayer){
		this.listAllinPotAmounts.add(allInPlayer);
	}
   
   
   public int  getPlayerWinningAmount(String playerName){
		int winningAmount = 0;
		for(Winner winner:listWinners){
			if(winner.getPlayer().getPlayeName().equals(playerName)){
			    winningAmount=winner.getWinningAmount();
				
			}
		}
		return winningAmount;
	}
   
   
   public int  getAllInPotAmount(String playerName){
		int allInPotAmount = 0;
		for(AllInPlayer allInplayer:listAllinPotAmounts){
			if(allInplayer.getPlayeName().equals(playerName)){
				allInPotAmount=allInplayer.getTotalAllInPotAmount();
				
			}
		}
		return allInPotAmount;
	}
   
   
   
   public void setTotalTableAmount(int amount){
	   this.totalTableAmount=amount;
   }
   
   
   
   public void  setPlayerWinningAmount(String playerName,int winningAmount){
		
		for(Winner winner:listWinners){
			if(winner.getPlayer().getPlayeName().equals(playerName)){
			    winner.setWinningAmount(winningAmount);
				break;
			}
		}
		
	}
   
   public void findWinnerPlayers() {
	   boolean findFinalWinner=false;
//		Collections.sort(playerManager.getAllAvailablePlayers(),
//				new Comparator<PlayerBean>() {
//					@Override
//					public int compare(PlayerBean paramT1, PlayerBean paramT2) {
//						return paramT1.getHandRank().compareTo(
//								paramT2.getHandRank());
//					}
//				});
//
//		listAllSortedRankPlayers=playerManager.getAllAvailablePlayers();
//		
//        for (PlayerBean player : listAllSortedRankPlayers) {
//			
//			System.out.println("\n winner are =     "+player.getHandRank());   
//		}
//        System.out.println("\n ---------------------------------");   
//        
//        
//        
//        findSameRankWinners();
//         
//  for (PlayerBean player : playerManager.getAllAvailablePlayers()) {
//			
//			System.out.println("\n winner are =     "+player.getHandRank());   
//		}
//        
//        System.out.println("\n ---------------------------------");   
//        
//		// by nilesh to compare wiiners	
//
//		CompareWinners();
		
		while(!findFinalWinner){
			
			find();
		  for (PlayerBean player : playerManager.getAllAvailablePlayers()) {
		           if( player.isPlayerActive()	&& !player.isPlayrAllIn()){
		        	   findFinalWinner=true;
		            }
		      listAllFinalizePlayers.add(player);
		      listAllSortedRankPlayers.remove(player);
		  }
		    if(!findFinalWinner){
		          playerManager.setAllAvailablePlayers(listAllSortedRankPlayers);  
		       }
		  
	  
		}
		  
		for (PlayerBean player : listAllFinalizePlayers) {
					
			if (player.isPlayerActive()) {
				if(!player.isPlayrAllIn()){
				    Winner winner = new Winner(player,totalTableAmount );
				    winner.getPlayer().setTotalBalance(winner.getPlayer().getTotalBalance()+winner.getWinningAmount());
				    totalTableAmount=0;
				    listWinners.add(winner);
				    break;
				  }else{
					 if(getAllInPotAmount(player.getPlayeName())<totalTableAmount){
				    Winner winner = new Winner(player,getAllInPotAmount(player.getPlayeName()) );
				    winner.getPlayer().setTotalBalance(winner.getPlayer().getTotalBalance()+winner.getWinningAmount());
				    totalTableAmount-=getAllInPotAmount(player.getPlayeName());
					listWinners.add(winner);
					}else{
				     	  Winner winner = new Winner(player,totalTableAmount);
						  winner.getPlayer().setTotalBalance(winner.getPlayer().getTotalBalance()+winner.getWinningAmount());
					      totalTableAmount=0;
						  listWinners.add(winner);
						  break;
					  }
				}
			}
		}
		
		if(totalTableAmount!=0){
			remainingAmount=totalTableAmount;
		}
		
		  System.out.println("\n ---------------------------------");   
		 for (AllInPlayer player : listAllinPotAmounts) {
				
				System.out.println("\n All In pot amount are =     "+player.getTotalAllInPotAmount());   
			}

	}
   
   
   public void find(){
	   Collections.sort(playerManager.getAllAvailablePlayers(),
				new Comparator<PlayerBean>() {
					@Override
					public int compare(PlayerBean paramT1, PlayerBean paramT2) {
						return paramT1.getHandRank().compareTo(
								paramT2.getHandRank());
					}
				});

	   
	    listAllSortedRankPlayers.clear();
	    listAllSortedRankPlayers.addAll(playerManager.getAllAvailablePlayers());
	
		
       for (PlayerBean player : listAllSortedRankPlayers) {
			
			System.out.println("\n winner are =     "+player.getHandRank());   
		}
       System.out.println("\n ---------------------------------");   
       
       
       
       findSameRankWinners();
        
 for (PlayerBean player : playerManager.getAllAvailablePlayers()) {
			
			System.out.println("\n winner are =     "+player.getHandRank());   
		}
       
       System.out.println("\n ---------------------------------");   
       
		// by nilesh to compare wiiners	

		CompareWinners();
   }
   
   
//   public void distributeWinningAmountToAllWinners(){
//	   for(Winner winner: listWinners){
//		   winner.getPlayer().setTotalBalance(winner.getPlayer().getTotalBalance()+winner.getWinningAmount());
//	   }
//   }
   
   public void findSameRankWinners(){
	   
	   
	   ArrayList<PlayerBean> listPlayers = new ArrayList<PlayerBean>();
	   listPlayers.clear();
		for (int i = 0; i < playerManager.getAllAvailablePlayers().size(); i++) {
			listPlayers.add(playerManager.getAllAvailablePlayers().get(i));
		}
	  PlayerBean player=playerManager.getAllAvailablePlayers().get(0);
	  for(PlayerBean playerTemp:listPlayers){
		  if(player.getHandRank()!=playerTemp.getHandRank()){
			  playerManager.getAllAvailablePlayers().remove(playerTemp);
		  }
		  
	  }
   }
   
   
   public void CompareWinners() {
		ArrayList<ArrayList<Card>> ListOfCardArray = new ArrayList<ArrayList<Card>>();
		ListOfCardArray.clear();
		for (int i = 0; i < playerManager.getAllAvailablePlayers().size(); i++) {
			ListOfCardArray.add(playerManager.getAllAvailablePlayers().get(i).getBestHandCards());
		}

		for (int j = 0, k = 0; j < 5; j++, k = 0) {

			while (k < ListOfCardArray.size() - 1) {
				if (ListOfCardArray.get(k).get(j).getValue() > ListOfCardArray
						.get(k + 1).get(j).getValue()) {
					ListOfCardArray.remove(k + 1);
					playerManager.getAllAvailablePlayers().remove(k + 1);
					k = 0;
					continue;
				} else if (ListOfCardArray.get(k).get(j).getValue() < ListOfCardArray
						.get(k + 1).get(j).getValue()) {
					ListOfCardArray.remove(k);
					playerManager.getAllAvailablePlayers().remove(k);
					k = 0;
					continue;
				} else {

				}
				k++;
			}
		}
		
		for (PlayerBean player : playerManager.getAllAvailablePlayers()) {
			
			System.out.println("\n winner are =     "+player.getHandRank());   
		}
	}
   
   
   
   
   public ArrayList<Winner> getWinnerList(){
	   return listWinners;
   }
   
   public Winner getTopWinner(){
	   return listWinners.get(0);
   }
   
  
}
