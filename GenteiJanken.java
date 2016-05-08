package labo;

import java.util.Random;

public class GenteiJanken {
	/********************************************************
	 *	限定ジャンケン
	 *	グー・チョキ・パーのカードがそれぞれ4枚ずつ
	 *	星は初めに3個
	 *	所持金は乱数で決まる。
	 *
	 *
	 *
	 *********************************************************/
	final static int PLAY_NUM = 100;
	final static boolean DEBUGOUT = true;
	static Random rnd = new Random();
	private static class Player{
		//	PlayerのNo
		int no;
		// 生存判定
		boolean live;
		// 成功判定
		boolean success;
		//	それぞれグー・チョキ・パーのカード枚数
		int card[] = new int[3];
		//	星の個数
		int star;
		//	所持金
		int money;
		// 開始時所持金
		int startMoney;
		// 星の買取価格
		int StarValue;
		Player(int no){
			this.no = no;
			this.live = true;
			this.success = false;
			for(int i=0;i<3;i++){
				this.card[i] = 4;
			}
			this.star = 3;
			// 所持金は乱数で決まる。
			this.money = (int) (Math.round(1+Math.pow(rnd.nextInt(60), 1.5))*1000);
			this.startMoney = this.money;
			// 星の買取価格は自分の所持金/星の個数
			this.StarValue = this.money / this.star;
		}
		// バトル時にどのカードを選ぶか
		// 一番多く持ってるカードを使う
		int select_card(){
			int max = Integer.MIN_VALUE;
			int max_no = 0;
			// 全て同じ枚数なら乱数で選ぶ
			if(this.card[0] == this.card[1] && this.card[1] == this.card[2]){
				max_no = rnd.nextInt(3);
			}
			else{
				for(int i=0;i<3;i++){
					if(max < this.card[i]){
						max = this.card[i];
						max_no = i;
					}
				}
			}
			if(this.card[max_no]<=0){
				return -1;
			}
			// 使用したカードが一つ減る
			this.card[max_no]--;
			// 使用するカードNoが返る
			return max_no;
		}
		// 星の相場チェック
		void setStarValue(){
			if(this.star > 0){
				this.StarValue =  this.money / this.star;
			}
			else{
				this.StarValue = Integer.MAX_VALUE;
			}
		}
		void showinfo(){
			System.out.println("No."+this.no+":グー:"+this.card[0]+"チョキ:"+this.card[1]+"パー"+
					this.card[2]+"星:"+this.star+"所持金:"+this.money);
		}
		void showresult(){
			System.out.print("No."+this.no);
			if(this.success){
				System.out.print("WINNER!");
			}
			else{
				System.out.print("Lose...");
			}
			System.out.println(":グー:"+this.card[0]+"チョキ:"+this.card[1]+"パー"+
					this.card[2]+"星:"+this.star+"所持金:"+this.money+"利益:"+(this.money-this.startMoney));
		}
		// 終了判定
		void EndCheck(){
			if(this.star <= 0){
				this.live = false;
				this.success = false;
				if(DEBUGOUT){
					System.out.println(this.no+":LOSE...");
				}
			}
			else if(this.card[0]<=0&&this.card[1]<=0&&this.card[2]<=0){
				this.live = false;
				if(this.star >=3){
					this.success = true;
					if(DEBUGOUT){
						System.out.println(this.no+":SUCCESS!");
					}
				}
				else{
					this.success = false;
					if(DEBUGOUT){
						System.out.println(this.no+":LOSE...");
					}
				}
			}
		}
	}
	// PlayerAとPlayerBがバトル
	public static void Battle(Player a,Player b){
		int ACard = a.select_card();
		int BCard = b.select_card();
		// 異常
		if(ACard == -1 || BCard == -1){
			System.out.println("異常");
			a.showinfo();
			b.showinfo();
			System.exit(0);
		}
		// 引き分けの時
		if(ACard == BCard){
			if(DEBUGOUT){
				System.out.println("引き分け");
				a.showinfo();
				b.showinfo();
			}
		}
		// A勝利の時
		else if(ACard == 0 && BCard == 1||
				ACard == 1 && BCard == 2||
				ACard == 2 && BCard == 0){
			a.star++;
			b.star--;
			if(DEBUGOUT){
				System.out.println(a.no+"勝利");
				a.showinfo();
				b.showinfo();
			}
		}
		// B勝利の時
		else{
			a.star--;
			b.star++;
			if(DEBUGOUT){
				System.out.println(b.no+"勝利");
				a.showinfo();
				b.showinfo();
			}
		}
		// 互いの終了判定
		a.EndCheck();
		b.EndCheck();
	}
	public static void main(String[] args) {
		// 参加者100人生成
		Player player[] = new Player[PLAY_NUM];
		for(int i=0;i<PLAY_NUM;i++){
			player[i] = new Player(i);
		}
		// 生存者数
		int liveNum = PLAY_NUM;
		// バトルカウント
		int BCount =1;
		/********************ゲーム開始********************/
		while(liveNum >= 2){
			// 対戦相手決定
			int pear[] = new int[2];
			do{
				pear[0] = rnd.nextInt(PLAY_NUM);
			}while(!player[pear[0]].live);
			do{
				pear[1] = rnd.nextInt(PLAY_NUM);
			}while(!(player[pear[1]].live) || (pear[0]==pear[1]));
			// バトル
			if(DEBUGOUT){
				System.out.print(BCount+"戦目：");
			}
			Battle(player[pear[0]],player[pear[1]]);
			if(DEBUGOUT){
				System.out.println("----------------------------------------");
			}
			// 星の相場チェック
			for(int i=0;i<PLAY_NUM;i++){
				player[i].setStarValue();
			}
			// 星が無くなった奴は買い取り
			for(int i=0;i<PLAY_NUM;i++){
				int cardnum = player[i].card[0] + player[i].card[1] + player[i].card[2];
				if(player[i].star==0&&cardnum!=0){
					for(int j=0;j<PLAY_NUM;j++){
						if(player[j].star > 3 && player[i].money >= player[j].StarValue){
							player[i].money -= player[j].StarValue;
							player[j].money += player[j].StarValue;
							player[i].star++;
							player[j].star--;
							player[i].live = true;
							if(DEBUGOUT){
								System.out.println("星売買成立："+player[j].no+"→"+player[i].no+"成立価格:"+player[j].StarValue);
							}
							break;
						}
					}
				}
			}
			// 生存者チェック
			liveNum = 0;
			for(int i=0;i<PLAY_NUM;i++){
				liveNum = player[i].live?liveNum+1:liveNum;
			}
			BCount++;
		}
		// 星の相場チェック
		for(int i=0;i<PLAY_NUM;i++){
			player[i].setStarValue();
		}
		System.out.println("*****星オークション*****");
		// 最後の星オークション
		for(int i=0;i<PLAY_NUM;i++){
			if(!player[i].success && player[i].card[0]==0&&
					player[i].card[1]==0&&player[i].card[2]==0){
				int need_star = 3 - player[i].star;
				for(int j=0;j<PLAY_NUM;j++){
					if(player[j].star > (need_star+2) &&
							player[i].money >= (player[j].StarValue*need_star)){
						player[i].money -= player[j].StarValue*need_star;
						player[j].money += player[j].StarValue*need_star;
						player[i].star+=need_star;
						player[j].star-=need_star;
						player[i].success = true;
						if(DEBUGOUT){
							System.out.println("星売買成立："+player[j].no+"→"+player[i].no+"成立価格:"+player[j].StarValue+"個数:"+need_star);
						}
						player[j].setStarValue();
						break;
					}
				}
			}
		}
		//	結果発表
		System.out.println("*****結果発表*****");
		int ownMoney =0,losenum=0,plusnum=0;
		for(int i=0;i<PLAY_NUM;i++){
			player[i].showresult();
			if(!player[i].success){
				ownMoney += player[i].money;
				losenum++;
			}
			else if(player[i].money - player[i].startMoney > 0){
				plusnum++;
			}
		}
		System.out.println("胴元利益:"+ownMoney);
		System.out.println("敗北者:"+losenum);
		System.out.println("利益が出た人数:"+plusnum);
		System.out.println("勝利はしたが損失を出した数:"+(PLAY_NUM-(losenum+plusnum)));
	}

}
