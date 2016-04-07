package pokerserver.players;

import java.util.ArrayList;
import java.util.List;

public class WAShortPotBean {

	int waCardAmt = 0;
	PlayerBean player;
	int playerWACardAmt = 0;
	List<List<ContributionBean>> listContributedPlayers;
	int totalShortPotAmt = 0;
	
	public WAShortPotBean(PlayerBean playerBean,int waCardAmt ,int playerWACardAmt) {
		this.player = playerBean;
		this.waCardAmt= waCardAmt;
		this.playerWACardAmt = playerWACardAmt;
		this.listContributedPlayers = new ArrayList<List<ContributionBean>>();
	}

	public int getWaCardAmt() {
		return waCardAmt;
	}

	public void addContributedPlayerDetails(PlayerBean player,int diffAmt){
		ContributionBean contributionBean = new ContributionBean(player,diffAmt);
		if(!checkSameDiffAmtContributionAvailable(contributionBean)){
			List<ContributionBean> listContribution = new ArrayList<ContributionBean>();
			listContribution.add(contributionBean);
			listContributedPlayers.add(listContribution);
		}
		
	}
	
	public boolean checkSameDiffAmtContributionAvailable(ContributionBean contributionBean){
		for(List<ContributionBean> listContribution : listContributedPlayers){
			if(listContribution.get(0).getDiffAmt()==contributionBean.getDiffAmt() && 
					!listContribution.contains(contributionBean)){
				listContribution.add(contributionBean);
				return true;
			}
		}
		return false;
	}
	
	public void distributeShortPotToPlayer(List<PlayerBean> listAscWinningPlayers){
		System.out.println("======= WA Short Card ==========");
		System.out.println("WinnerPlayer : "+ player.getPlayerName()+" >> WA Amt : "+playerWACardAmt +" >> Short Pot : "+totalShortPotAmt);
		for(List<ContributionBean> listContribution : listContributedPlayers){
			if(listContribution.size()==1){
				PlayerBean playerBean =listContribution.get(0).getPlayerBean();
				System.out.println("Distributed Plr :1 : "+ playerBean.getPlayerName() +" >> Balance : "+playerBean.getTotalBalance());
				playerBean.setTotalBalance(playerBean.getTotalBalance()+totalShortPotAmt);
				System.out.println(">> Balance : "+playerBean.getTotalBalance());
			}else if(!listContribution.isEmpty()){
				for(PlayerBean playerBean : listAscWinningPlayers){
					boolean playerFound = false;

					for (ContributionBean contributionBean : listContribution) {
						if (contributionBean.getPlayerBean().equals(playerBean)) {
							System.out.println("Distributed Plr :2 : "+ playerBean.getPlayerName() +" >> Balance : "+playerBean.getTotalBalance());
							playerBean.setTotalBalance(playerBean
									.getTotalBalance()
									+ totalShortPotAmt);
							System.out.println(">> Balance : "+playerBean.getTotalBalance());
							playerFound = true;
							break;
						}
					}
					if(playerFound)
						break;
				}
			}
		}
	}
	public List<List<ContributionBean>> getContributionPlayers(){
		return listContributedPlayers;
	}
	public void setShortPotAmt(int totalShortAmt){
		this.totalShortPotAmt=totalShortAmt;
	}
	
	public int getTotalShortPotAmt(){
		return totalShortPotAmt;
	}
	
	public void setWaCardAmt(int waCardAmt) {
		this.waCardAmt = waCardAmt;
	}

	public PlayerBean getPlayer() {
		return player;
	}

	public void setPlayer(PlayerBean player) {
		this.player = player;
	}

	public int getPlayerWACardAmt() {
		return playerWACardAmt;
	}

	public void setPlayerWACardAmt(int playerWACardAmt) {
		this.playerWACardAmt = playerWACardAmt;
	}

//	public int getShortAmt() {
//		return shortAmt;
//	}

//	public void setShortAmt(int shortAmt) {
//		this.shortAmt = shortAmt;
//	}
	class ContributionBean{
		PlayerBean playerBean;
		int diffAmt;
		
		public ContributionBean(PlayerBean playerBean,
		int diffAmt) {
			this.playerBean=playerBean;
			this.diffAmt = diffAmt;
		}

		public PlayerBean getPlayerBean() {
			return playerBean;
		}

		public int getDiffAmt() {
			return diffAmt;
		}
		
	}
}

