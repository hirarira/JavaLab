package labo;

import java.util.Random;

/*
 * バトル大会
 * プレイヤーXXXX人はLv1からスタートする
 * 同じレベル同士のプレイヤーがバトルし、勝者のレベルが1上がり
 * 敗者のレベルは1下がる。
 * 異なるレベル同士のプレイヤーで戦った場合、両者のレベルが勝者が2人の内
 * 高い方のレベルへ、敗者が低い方のレベルへ入れ替わる。
 * Lv1同士のプレイヤーが戦い、負けた場合敗者は死亡する。
 * Lvが10に達した時点で勝利となる。
 *
 */

public class Battle {
	static Random rnd = new Random();
	final static int MAX_PLAER = 100;
	final static int START_LV = 1;
	final static int MAX_LV = 10;
	// 生存人数
	static int live_num = MAX_PLAER;
	// どのレベルに何人いるか
	static int[] lv_list = new int[MAX_LV+1];
	static public class Player{
		// レベル
		int lv;
		// 強さ(1 - 1000)
		int st;
		// 生存
		boolean live;
		boolean win;
		//戦績
		int win_num;
		int lose_num;
		Player(){
			this.lv = START_LV;
			this.st = rnd.nextInt(1000) + 1;
			this.live = true;
			this.win = false;
			this.win_num = 0;
			this.lose_num = 0;
		}
		void lv_change(boolean win){
			lv_list[ this.lv ]--;
			this.lv = (win) ? this.lv+1:this.lv-1;
			lv_list[ this.lv ]++;
			if(this.lv <= 0 || this.lv >= MAX_LV){
				live = false;
				live_num--;
				if(this.lv >= MAX_LV){
					this.win = true;
				}
			}
		}
		void show_info(int no){
			System.out.print("no."+no+"	Lv:"+this.lv+"	st:"+this.st);
		}
		void power_up(){
			this.st+=10;
		}
	}
	public static void main(String[] args){
		int battle_count =0;
		// 対戦相手No. (Battle Actor)
		int ba1 = 0,ba2 = 0;
		int win = 0,lose = 0;
		// 初期設定
		Player pl[] = new Player[MAX_PLAER];
		for(int i=0;i<MAX_PLAER;i++){
			pl[i] = new Player();
		}
		lv_list[START_LV] = MAX_PLAER;
		boolean game_loop = true;
		// バトル開始
		while(live_num > 0 && game_loop){
			// 対戦相手決定
			do{
				ba1 = rnd.nextInt(MAX_PLAER);
				ba2 = rnd.nextInt(MAX_PLAER);
			}while( (ba1 == ba2) || (!pl[ba1].live) || (!pl[ba2].live) ||
					(pl[ba1].lv - pl[ba2].lv) > 1 || (pl[ba2].lv - pl[ba1].lv) > 1);
			pl[ba1].show_info(ba1);
			System.out.print("	vs	");
			pl[ba2].show_info(ba2);
			// 戦闘勝敗		確率による勝敗判定
			int result = rnd.nextInt( pl[ba1].st + pl[ba2].st );
			// 勝利判定
			if( result < pl[ba1].st ){
				win = ba1;
				lose = ba2;
			}
			else{
				win = ba2;
				lose = ba1;
			}

//			// 戦闘勝敗		STによる絶対的な勝敗判定
//			if(pl[ba1].st > pl[ba2].st){
//				win = ba1;
//				lose = ba2;
//			}
//			else{
//				win = ba2;
//				lose = ba1;
//			}

			// 勝率処理
			pl[win].win_num++;
			pl[lose].lose_num++;
			// レベル処理
			if( pl[win].lv == pl[lose].lv ){
				pl[win].lv_change(true);
				pl[lose].lv_change(false);
			}
			else{
				if(pl[win].lv < pl[lose].lv){
					int sw = pl[win].lv;
					pl[win].lv= pl[lose].lv;
					pl[lose].lv = sw;
				}
			}
			// 硬直化判定
			game_loop = false;
			for(int i=1;i<MAX_LV;i++){
				if(lv_list[i]>1){
					game_loop = true;
				}
			}
			// 敗者は少し強くなる
			pl[lose].power_up();
			battle_count++;
			System.out.println("	Win:No."+win+"	Lose:No."+lose+"	Now_live:"+live_num+"	Battle_Count:"+battle_count);
		}
		for(int i=0;i<MAX_LV+1;i++){
			System.out.println("Lv"+i+"	:"+lv_list[i]);
		}
		// 結果表示
		for(int i=0;i<MAX_PLAER;i++){
			pl[i].show_info(i);
			System.out.println("	Win:"+pl[i].win_num+"	Lose:"+pl[i].lose_num);
		}
	}

}
