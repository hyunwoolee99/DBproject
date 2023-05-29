
	 
	/*
	 *	This content is generated from the API File Info.
	 *	(Alt+Shift+Ctrl+I).
	 *
	 *	@desc 		
	 *	@file 		untitled
	 *	@date 		1653379695405
	 *	@title 		Page 1
	 *	@author 	
	 *	@keywords 	
	 *	@generator 	Export Kit v1.3.figma
	 *
	 */
	

package exportkit.xd;

import static java.sql.Types.NULL;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


	public class untitled_activity extends Activity {


		private RecipeDao mRecipeDao;
		private StepDao mStepDao;
		private IngredientDao mIngredientDao;
		private UserDao mUserDao;
		private BookmarkDao mBookmarkDao;
		private DontwantDao mDontwantDao;
		private QueryDao mQueryDao;

		User signedInUser = null;


		public Boolean userIdDuplicateCheck(String userID) {
			SimpleSQLiteQuery query = new SimpleSQLiteQuery("select id from User where id = \"" + userID + "\"", new Object[]{});
			List<String> dup = mQueryDao.getUserIdDuplicate(query);
			if (dup.size() != 0) {
				if (dup.get(0) == userID)
					return false;
			}
			return true;
		}

		public Boolean signUp(String userID, String password, String name) {
			if(userIdDuplicateCheck(userID)){
				User newUser = new User();
				newUser.setId(userID);
				newUser.setPassword(password);
				newUser.setName(name);
				newUser.setBeginner(false);
				mUserDao.setInsertUser(newUser);
				return true;
			}
			else
				return false;
		}

		public Boolean signIn(String userID, String password) {
			SimpleSQLiteQuery query = new SimpleSQLiteQuery("select * from user where id = \"" + userID + "\" AND password = \"" + password + "\"", new Object[]{});
			List<User> userInfo = mQueryDao.getUser(query);
			if (userInfo.size() != 0) {
				signedInUser = userInfo.get(0);
				return true;
			}
			return false;
		}

		public void signOut() {
			signedInUser = null;
		}

		public Boolean withdrawal(String password) {
			if (signedInUser != null) {
				if (signedInUser.getPassword().equals(password)) {
					mUserDao.setDeleteUser(signedInUser);
					signOut();
					return true;
				}
			}
			return false;
		}

		public void biginnerCheck(String userID) {
			SimpleSQLiteQuery query = new SimpleSQLiteQuery("select * from user where id = \"" + userID + "\"", new Object[]{});
			List<User> begin = mQueryDao.getUser(query);
			if (begin.size() != 0){
				if (begin.get(0).getBeginner() == true){
					begin.get(0).setBeginner(false);
					mUserDao.setUpdateUser(begin.get(0));
				}
				else {
					begin.get(0).setBeginner(true);
					mUserDao.setUpdateUser(begin.get(0));
				}
				updateSignedInUser();
			}
		}

		public void updateSignedInUser() {
			SimpleSQLiteQuery query = new SimpleSQLiteQuery("select * from user where id = \"" + signedInUser.getId() + "\"", new Object[]{});
			List<User> userInfo = mQueryDao.getUser(query);
			signedInUser = userInfo.get(0);
		}


		public void insertBookmark(String userID, int recipe_id) {
			if (userID != null){
				Bookmark newBookmark = new Bookmark();
				newBookmark.setUser_ID(userID);
				newBookmark.setRecipe_ID(recipe_id);
				mBookmarkDao.setInsertBookmark(newBookmark);
			}
		}

		public void deletBookmark(String userID, int recipe_id) {
			Bookmark deletB = new Bookmark();
			deletB.setUser_ID(userID);
			deletB.setRecipe_ID(recipe_id);
			mBookmarkDao.setDeleteBookmark(deletB);
		}

		public List<Integer> searchRecipeName (String name) {
			String SearchbyName = "SELECT DISTINCT ID FROM Recipe WHERE name LIKE \"%";
			String end = "%\"";
			SearchbyName = SearchbyName + name + end;
			SimpleSQLiteQuery query = new SimpleSQLiteQuery(SearchbyName, new Object[]{});
			List<Integer> idList = mQueryDao.getSearchbyName(query);

//        for (int i = 0; i < idList.size(); i++) {
//            Log.d("Test", idList.get(i) + "\n");
//        }
			return idList;
		}

		public List<Integer> searchIngredient (String[] wantList, String[] dontwantList) {
			String searchIngrdient = "SELECT distinct recipe_ID FROM ingredient";
			String where = " WHERE ";
			String and = " AND ";
			String w = "recipe_ID in (select recipe_ID FROM ingredient where name LIKE \"%";
			String dw = "recipe_ID not in (select recipe_ID FROM ingredient where name LIKE \"%";
			String end = "%\")";

			if (wantList != null || dontwantList != null) {
				searchIngrdient += where;
				if (wantList != null) {
					for (int i = 0; i < wantList.length; i++) {
						if (i == 0)
							searchIngrdient = searchIngrdient + w + wantList[i] + end;
						else
							searchIngrdient = searchIngrdient + and + w + wantList[i] + end;
					}
				}
				if (dontwantList != null) {
					if (wantList != null) {
						for (int i = 0; i < dontwantList.length; i++) {
							searchIngrdient = searchIngrdient+ and + dw + dontwantList[i] + end;
						}
					}
					else {
						for (int i = 0; i < dontwantList.length; i++) {
							if (i == 0)
								searchIngrdient = searchIngrdient + dw + dontwantList[i] + end;
							else
								searchIngrdient = searchIngrdient + and + dw + dontwantList[i] + end;
						}
					}
				}
			}

			SimpleSQLiteQuery query = new SimpleSQLiteQuery( searchIngrdient, new Object[]{});
			List<Integer> idList = mQueryDao.getsearchIngredient(query);

//        for (int i = 0; i < idList.size(); i++) {
//            Log.d("Test", idList.get(i) + "\n");
//        }
			return idList;
		}


		public void getStepByID(int id) {
			String getStepbyID = "SELECT * FROM Step WHERE recipe_ID=\"";
			String endid = "\"";
			String order = "ORDER BY step_no";
			getStepbyID = getStepbyID + Integer.toString(id) + endid + order;
			SimpleSQLiteQuery query = new SimpleSQLiteQuery(getStepbyID, new Object[]{});
			List<Step> stepList = mQueryDao.getStepbyID(query);

			for (int i = 0; i < stepList.size(); i++) {
				Log.d("Test", stepList.get(i).getRecipe_ID() + "\n"
						+ stepList.get(i).getStep_no() + "\n"
						+ stepList.get(i).getStep_DC() + "\n");
			}
		}

		public void timer(int minute, int second) {
			LocalTime start = LocalTime.now();
			int endSecond = start.getSecond() + second;
			int endMinute = start.getMinute() + minute;
			while (LocalTime.now().getMinute() != minute && LocalTime.now().getSecond() != endSecond){
				try {
					Thread.sleep(1000);
					Log.d("time", "sleep");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Log.d("time", "after");
		}


		public List<Recipe> getRecipies(List<Integer> idList) {
			String getRecipebyID = "SELECT * FROM Recipe WHERE ID= ";
			String text;
			List<Recipe> recipeList = new ArrayList<Recipe>();
			for (int i = 0; i < idList.size(); i++) {
				String id = idList.get(i).toString();
				text = getRecipebyID + id;
				SimpleSQLiteQuery query = new SimpleSQLiteQuery(text, new Object[]{});
				Recipe recipe = mQueryDao.getRecipebyID(query);
				recipeList.add(recipe);
			}
			//Test
			for (int i = 0; i < recipeList.size(); i++) {
				Log.d("Test", recipeList.get(i).getName() + "\n"
						+ recipeList.get(i).getID() + "\n");
			}

			return recipeList;
		}



		public void onCreate(Bundle savedInstanceState) {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.loginout_home);

			RecipeDB database = Room.databaseBuilder(getApplicationContext(), RecipeDB.class, "recipe info")
					.fallbackToDestructiveMigration()
					.allowMainThreadQueries()
					.build();

			mRecipeDao = database.recipeDao();
			mStepDao = database.stepDao();
			mIngredientDao = database.ingredientDao();
			mUserDao = database.userDao();
			mBookmarkDao = database.bookmarkDao();
			mDontwantDao = database.dontwantDao();

			mQueryDao = database.queryDao();



//        String[] wantList = {"간장"};
//        String[] dontwantList = {"설탕"};
//        searchIngredient(wantList, dontwantist);

//        String foodName = "김치";
//        searchRecipeName(foodName);

//        int recipe_id = 3;
//        getStepByID(recipe_id);

//        timer(0, 2);

//        insertBookmark("abc3", 14);

//        deletBookmark("abc3", 14);

//        Boolean va = userIdDuplicateCheck("abc11");
//        Log.d("boolean", String.valueOf(va));

//        signUp("abc11", "9823", "Son");

//        biginnerCheck("abc3");

//        signOut();
//        boolean login = signIn("abc11", "9823");
//        Log.d("login", String.valueOf(login));
//        withdrawal("9823");
//        if (signedInUser == null) Log.d("withdrawl", "true");

//        String[] wantList = {"간장"};
//        String[] dontwantList = {"설탕"};
//        getRecipies(searchIngredient(wantList, null));
//        getRecipies(searchRecipeName("냉국"));

//			insertDB();

		}

		public void insertDB() {
			Recipe re = new Recipe();

			re.setID(1);
			re.setName("나물비빔밥");
			re.setSummary("육수로 지은 밥에 야채를 듬뿍 넣은 영양만점 나물비빔밥!");
			re.setNation_code(3020001);
			re.setNation_name("한식");
			re.setTY_code(3010001);
			re.setTY_name("밥");
			re.setTime(60);
			re.setCalorie(580);
			re.setQNT(4);
			re.setLevel("보통");
			re.setPC_NM(5000);
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/000200.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(2);
			re.setName("오곡밥");
			re.setSummary("정월대보름에 먹던 오곡밥! 영양을 한그릇에 담았습니다.");
			re.setCalorie(338);
			re.setPC_NM(3000);
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/000300.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(3);
			re.setName("잡채밥");
			re.setSummary("잡채밥 한 그릇이면 오늘 저녁 끝! 입 맛 없을 때 먹으면 그만이지요~");
			re.setNation_code(3020004);
			re.setNation_name("중국");
			re.setTime(30);
			re.setCalorie(520);
			re.setPC_NM(NULL);
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/000400.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(4);
			re.setName("콩나물밥");
			re.setSummary("다이어트에 으뜸인 콩나물밥. 밥 물 넣을때 평소보다 적게 넣는거 잊지마세요!");
			re.setNation_code(3020001);
			re.setNation_name("한식");
			re.setTime(40);
			re.setCalorie(401);
			re.setLevel("초보환영");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/000600.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(5);
			re.setName("약식");
			re.setSummary("집에서도 쉽게 만들어 맛있게 먹을 수 있답니다. 어려워 마시고 만들어 보세요~!");
			re.setTY_code(3010013);
			re.setTY_name("떡/한과");
			re.setTime(60);
			re.setCalorie(259);
			re.setLevel("보통");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/000800.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(6);
			re.setName("호박죽");
			re.setSummary("호박죽 한 그릇이면 하루가 든든하답니다.");
			re.setTY_code(3010001);
			re.setTY_name("밥");
			re.setTime(30);
			re.setCalorie(115);
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/001300.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(7);
			re.setName("흑임자죽");
			re.setSummary("검은깨를 갈아서 만든 고소함이 가득한 흑임자죽. 남녀노소 모두 사랑하는 맛!");
			re.setTime(25);
			re.setCalorie(450);
			re.setIRDNT_code(null);
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/001400.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(8);
			re.setName("카레라이스");
			re.setSummary("향긋한 카레향이 너무 좋지요. 누구나 좋아하는 만들기도 간편한 음식입니다.");
			re.setNation_code(3020005);
			re.setNation_name("동남아시아");
			re.setTime(30);
			re.setCalorie(650);
			re.setLevel("초보환영");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/001600.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(9);
			re.setName("오므라이스");
			re.setSummary("각종 채소를 계란 속에 꼭꼭 숨겨 편식하는 아이들도 맛있게 먹어요~");
			re.setNation_code(3020002);
			re.setNation_name("서양");
			re.setTime(30);
			re.setCalorie(630);
			re.setIRDNT_code("곡류");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/001800.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(10);
			re.setName("감자수제비");
			re.setSummary("쫀득쫀득한 수제비와 담백한 맛의 감자가 이뤄내는 하모니!");
			re.setNation_code(3020001);
			re.setNation_name("한식");
			re.setTY_code(3010016);
			re.setTY_name("만두/면류");
			re.setTime(60);
			re.setCalorie(410);
			re.setIRDNT_code("밀가루");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/001900.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(11);
			re.setName("냉면");
			re.setSummary("더운 여름, 살얼음 동동 띄운 시 원한 냉면 한그릇 생각나시죠~");
			re.setTime(50);
			re.setCalorie(630);
			re.setLevel("보통");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/002100.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(12);
			re.setName("동치미막국수");
			re.setSummary("시원한 동치미에 쫄깃한 국수를 말아서 만들어보세요.");
			re.setTime(30);
			re.setCalorie(400);
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/002400.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(13);
			re.setName("열무김치냉면");
			re.setSummary("맛있게 담근 열무김치에 냉면을 말아 먹어 보세요~ 새콤달콤 끝내줍니다!");
			re.setTime(25);
			re.setCalorie(625);
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/002800.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(14);
			re.setName("채소국수");
			re.setSummary("갖가지 야채를 듬뿍 넣어서 만든 요리로 출출할 때 간식거리로 아주 좋답니다.");
			re.setTime(30);
			re.setCalorie(460);
			re.setQNT(2);
			re.setLevel("초보환영");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/002900.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(15);
			re.setName("해물국수");
			re.setSummary("해물로 시 원한 국물에 국수를 말아 드셔보세요~");
			re.setTime(40);
			re.setCalorie(530);
			re.setQNT(4);
			re.setLevel("보통");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/003000.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(16);
			re.setName("만둣국");
			re.setSummary("만두를 예쁘게 만들면 이쁜 딸을 낳는다고 하죠? 가족들과 오손도손 맛있는 만두국 만들어 드셔보세요~");
			re.setCalorie(540);
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/003500.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(17);
			re.setName("다시마냉국");
			re.setSummary("철 분과 무기질이 풍부한 다시마로 피부건강을 지켜보세요~");
			re.setTY_code(3010002);
			re.setTY_name("국");
			re.setTime(20);
			re.setCalorie(63);
			re.setLevel("초보환영");
			re.setIRDNT_code("해조류");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/003900.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(18);
			re.setName("두부국");
			re.setSummary("부드러운 두부로 맛나는 두부국을 끓여 단백함을 맛보세요.");
			re.setTime(40);
			re.setCalorie(120);
			re.setIRDNT_code(null);
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/004200.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(19);
			re.setName("두부조개탕");
			re.setSummary("국물이 정말 시 원해서 속풀이 식단으로도 안성맞춤!");
			re.setTime(30);
			re.setCalorie(130);
			re.setIRDNT_code("어류/패류");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/004400.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(20);
			re.setName("무맑은국");
			re.setSummary("고향의 맛을 느낄 수 있는 무국. 밥한그릇 뚝딱이죠!");
			re.setCalorie(75);
			re.setIRDNT_code("채소류");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/004600.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(21);
			re.setName("미역국");
			re.setSummary("미역국은 철 분이 풍부하게 함유된 음식이라 여자들에게 특히 좋은 음식이라 하지요.");
			re.setCalorie(95);
			re.setIRDNT_code("해조류");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/004700.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(22);
			re.setName("미역냉국");
			re.setSummary("더운 여름, 새콤달콤 시 원한 미역냉국으로 입맛을 살려보세요~");
			re.setCalorie(82);
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/004800.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(23);
			re.setName("생태국");
			re.setSummary("생태국에 콩나물을 넣어주면 시 원함이 배가 됩니다.");
			re.setCalorie(110);
			re.setLevel("보통");
			re.setIRDNT_code("어류/패류");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/005000.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(24);
			re.setName("연어까르파치오");
			re.setSummary("독특한 바질향이 연어에 쏘옥~ 올리브오일로 한층 부드러운 연어회랍니다.");
			re.setNation_code(3020006);
			re.setNation_name("이탈리아");
			re.setTY_code(3010007);
			re.setTY_name("나물/생채/샐러드");
			re.setCalorie(120);
			re.setQNT(2);
			re.setLevel("초보환영");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/005100.jpg");

			mRecipeDao.setInsertRecipe(re);

			re.setID(25);
			re.setName("오이냉국");
			re.setSummary("아삭아삭한 오이와 새콤달콤한 냉국의 조화!");
			re.setNation_code(3020001);
			re.setNation_name("한식");
			re.setTY_code(3010002);
			re.setTY_name("국");
			re.setTime(20);
			re.setCalorie(82);
			re.setQNT(4);
			re.setIRDNT_code("채소류");
			re.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/005200.jpg");

			mRecipeDao.setInsertRecipe(re);



			// step

			Step st = new Step();

			st.setRecipe_ID(1);
			st.setStep_no(1);
			st.setStep_DC("양지머리로 육수를 낸 후 식혀 기름을 걷어낸 후, 불린 쌀을 넣어 고슬고슬하게 밥을 짓는다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/000200_p01.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(1);
			st.setStep_no(2);
			st.setStep_DC("안심은 불고기 양념하여 30분간 재워 국물 없이 구워 한 김 식으면 한입 크기로 자른다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/000200_p02.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(1);
			st.setStep_no(3);
			st.setStep_DC("청포묵은 고기와 비슷한 크기로 잘라 끓는 물에 데쳐내고 계란은 노른자와 흰자를 분리해 지단부쳐 곱게 채썬다.");
			st.setImage_URL(null);

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(1);
			st.setStep_no(4);
			st.setStep_DC("콩나물과 숙주, 미나리는 데쳐서 국간장과 참기름으로 간하고, 고사리와 도라지는 참기름을 두른 프라이팬에 살짝 볶아놓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(1);
			st.setStep_no(5);
			st.setStep_DC("밥을 참기름으로 무쳐 그릇에 담고 준비한 재료를 고루 얹는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(2);
			st.setStep_no(1);
			st.setStep_DC("찹쌀과 멥쌀은 깨끗이 씻어 건져 놓는다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/000300_p01.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(2);
			st.setStep_no(2);
			st.setStep_DC("차수수는 붉은 물이 안 나올 때까지 깨끗이 씻어 놓는다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/000300_p02.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(2);
			st.setStep_no(3);
			st.setStep_DC("콩은 불려서 일어 건져놓고, 팥은 삶아서 건져놓고 삶은 물은 받아 놓는다.");
			st.setImage_URL(null);

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(2);
			st.setStep_no(4);
			st.setStep_DC("차조는 씻어서 잘 일은 후 건져놓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(2);
			st.setStep_no(5);
			st.setStep_DC("차조를 뺀 모든 재료를 고루 섞어 밥솥에 앉혀 놓고 팥 삶은 물에 소금(1/3작은술 정도)을 넣은 밥물을 붓는데, 밥물은 보통 짓는 밥물보다 20% 적게 붓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(2);
			st.setStep_no(6);
			st.setStep_DC("밥이 끓기 시작하면 차조를 고루 얹어 뜸을 들인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(3);
			st.setStep_no(1);
			st.setStep_DC("당면은 따뜻한 물에 불려 적당한 길이로 자른다.");
			mStepDao.setInsertStep(st);

			st.setRecipe_ID(3);
			st.setStep_no(2);
			st.setStep_DC("고기와 버섯, 채소는 모두 채썬다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(3);
			st.setStep_no(3);
			st.setStep_DC("달군 팬에 기름을 두르고 고기와, 버섯, 당근, 고추, 호박을 볶다가 숨이 죽으면 부추를 넣는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(3);
			st.setStep_no(4);
			st.setStep_DC("다진 파, 마늘, 생강을 넣고 소금과 통후추, 진간장을 넣어 간을 한 다음 당면을 넣어 볶아준다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(3);
			st.setStep_no(5);
			st.setStep_DC("뜨거운 밥을 그릇에 담고 잡채를 얹어 낸다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(4);
			st.setStep_no(1);
			st.setStep_DC("쌀은 미리 씻어 불려놓고 콩나물은 씻어 소금물에 살짝 데쳐 놓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(4);
			st.setStep_no(2);
			st.setStep_DC("쇠고기는 곱게 다져 파, 마늘, 진간장으로 양념하여 볶는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(4);
			st.setStep_no(3);
			st.setStep_DC("콩나물 삶은 물을 냄비에 붓고 쌀을 앉혀 밥을 짓다가 끓으면 삶은 콩나물과 쇠고기를 얹어 뜸들인다");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(4);
			st.setStep_no(4);
			st.setStep_DC("뜸이 들면 고루 섞어 그릇에 담고 양념장과 함께 낸다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(5);
			st.setStep_no(1);
			st.setStep_DC("흰 설탕에 물을 붓고 끓이다가 거품이 일면서 한 부분부터 타기 시작하면 불을 끈다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(5);
			st.setStep_no(2);
			st.setStep_DC("압력솥에 물기 뺀 쌀을 담고 분량의 흑설탕, 간장, 참기름, 식물성기름, 계핏가루를 넣고 ①에서 만든 카라멜소스를 부어 물이들게 고루 섞는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(5);
			st.setStep_no(3);
			st.setStep_DC("②에 밤, 대추, 잣을 넣고 섞는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(5);
			st.setStep_no(4);
			st.setStep_DC("물 3컵을 붓고 센불로 가열하다가 끓기 시작하면 중불로 줄여 30분 지난뒤 불을 끄고 남은 열로 뜸들인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(6);
			st.setStep_no(1);
			st.setStep_DC("청동호박은 반을 갈라 씨를 빼고 껍질을 벗긴다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(6);
			st.setStep_no(2);
			st.setStep_DC("찹쌀은 깨끗이 씻어 불렸다가 가루로 빻는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(6);
			st.setStep_no(3);
			st.setStep_DC("팥은 씻어 일어 물을 넣고 한소끔 끓으면 첫물은 버리고 다시 물을 부어 푹 끓인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(6);
			st.setStep_no(4);
			st.setStep_DC("호박은 1cm 두께로 썰어 냄비에 담고 물과 설탕을 넣어 푹 끓인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(6);
			st.setStep_no(5);
			st.setStep_DC("호박이 푹 무르면 찹쌀가루를 물에 풀어 넣고 팥도 함께 넣어 끓인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(6);
			st.setStep_no(6);
			st.setStep_DC("다 끓으면 설탕, 소금으로 간한다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(7);
			st.setStep_no(1);
			st.setStep_DC("쌀은 충분히 불려서 소쿠리에 건져 놓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(7);
			st.setStep_no(2);
			st.setStep_DC("깨는 깨끗이 일어 건져서 고소한 향이 나도록 볶는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(7);
			st.setStep_no(3);
			st.setStep_DC("분쇄기에 쌀과 깨를 따로따로 넣어 물을 조금씩 넣어가며 갈아 고운 체에 밭친다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(7);
			st.setStep_no(4);
			st.setStep_DC("밑이 두터운 냄비에 곱게 간 쌀과 물을 부어 나무주걱으로 저으며 끓인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(7);
			st.setStep_no(5);
			st.setStep_DC("냄비가 뜨거워 지면 갈은 깨를 조금씩 부어 멍울지지 않도록 가끔 저어주며 끓인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(7);
			st.setStep_no(6);
			st.setStep_DC("확 끓어오르면 불을 약하게 줄이고 잘 섞이도록 서서히 끓인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(8);
			st.setStep_no(1);
			st.setStep_DC("쇠고기와 채소는 도톰하게 깍뚝썰기하여 버터에 볶는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(8);
			st.setStep_no(2);
			st.setStep_DC("카레는 찬물 2컵에 물을 조금씩 넣어가며 풀어놓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(8);
			st.setStep_no(3);
			st.setStep_DC("①의 냄비에 완두를 넣고 물을 자작하게 붓고 끓이다가 ②를 붓고 더 끓인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(8);
			st.setStep_no(4);
			st.setStep_DC("국물이 되직하게 졸면 우유를 붓고 소금과 후춧가루로 간을 맞춘다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(8);
			st.setStep_no(5);
			st.setStep_DC("따뜻한 밥위에 완성된 카레를 넉넉히 얹어낸다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(9);
			st.setStep_no(1);
			st.setStep_DC("청피망, 홍피망, 양파, 오이, 당근은 잘게 다져 준비한다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/001800_p01.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(9);
			st.setStep_no(2);
			st.setStep_DC("프라이팬에 버터를 두르고 당근, 양파를 볶다가 양파가 투명해지면 남은 야채 재료를 넣고 볶는다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/001800_p02.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(9);
			st.setStep_no(3);
			st.setStep_DC("찬밥을 넣어 야채와 잘 섞은 후 소금, 후춧가루로 간을 약하게 맞춘다.");
			st.setImage_URL(null);

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(9);
			st.setStep_no(4);
			st.setStep_DC("계란을 멍울 없이 풀어 소금, 후춧가루로 간하고, 반쯤 익으면 계란 중앙에 밥을 놓고 잘 감싸서 접시에 담는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(9);
			st.setStep_no(5);
			st.setStep_DC("다시마와 멸치로 다시국물을 만들어 체에 거른 후 프라이팬에 육수 2컵을 넣고 쌈장을 잘 풀어 끓여준다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(9);
			st.setStep_no(6);
			st.setStep_DC("팔팔 끓어오르면 전분가루를 넣어 농도를 되직하게 만든 후 오므라이스 위에 뿌려준다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(10);
			st.setStep_no(1);
			st.setStep_DC("밀가루에 소금과 따뜻한 물을 넣어 말랑하게 반죽하여 젖은 면보에 싸 냉장고에 30분간 넣어둔다.");
			st.setStep_tip("양념장: 간장 5큰술, 고춧가루 1큰술, 참기름 1큰술, 다진고추 1큰술, 설탕 1작은술, 후춧가루 1작은술");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(10);
			st.setStep_no(2);
			st.setStep_DC("감자와 애호박은 도톰하게 반달썰기한다.");
			st.setStep_tip(null);

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(10);
			st.setStep_no(3);
			st.setStep_DC("실파는 4cm 길이로 자르고 홍고추는 어슷썬다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(10);
			st.setStep_no(4);
			st.setStep_DC("멸치는 장국을 끓여 국간장으로 간을 맞춘다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(10);
			st.setStep_no(5);
			st.setStep_DC("국물이 끓어오르면 감자를 넣고, 반쯤 익으면 수제비 반죽을 얇게 뜯어 넣는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(10);
			st.setStep_no(6);
			st.setStep_DC("호박을 넣어 파랗게 익으면 홍고추와 실파, 참기름을 넣고 불을 끈다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(11);
			st.setStep_no(1);
			st.setStep_DC("쇠고기는 삶아 건져 편육으로 썰고 국물은 식혀 기름을 걷어 육수로 준비한다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/002100_p01.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(11);
			st.setStep_no(2);
			st.setStep_DC("동치미무는 길쭉하고 얇게 썰고 오이는 어슷썰어 소금에 20분 동안 절였다가 물기를 꼭 짜서 살짝 볶는다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/002100_p02.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(11);
			st.setStep_no(3);
			st.setStep_DC("배는 납작하게 썰고 계란은 삶아 반 가른다.");
			st.setImage_URL(null);

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(11);
			st.setStep_no(4);
			st.setStep_DC("육수와 동치미국물을 섞어 소금과 설탕, 식초로 간을 맞춰 차게 둔다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(11);
			st.setStep_no(5);
			st.setStep_DC("냉면국수는 삶아 찬물에 비벼 빨듯이 헹군다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(11);
			st.setStep_no(6);
			st.setStep_DC("대접에 면을 담고 편육과 무, 오이, 배, 계란을 얹은 후 육수를 부어 낸다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(12);
			st.setStep_no(1);
			st.setStep_DC("쇠고기는 채썰어 분량의 재료로 갖은양념하여 약간 재웠다가 팬에 볶는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(12);
			st.setStep_no(2);
			st.setStep_DC("오이는 가늘게 채썰어 소금에 절였다가 물기를 꼭 짜고 살짝 볶아 식힌다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(12);
			st.setStep_no(3);
			st.setStep_DC("동치미무는 반달모양으로 얄팍하게 썰어 깨소금과 참기름으로 무친다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(12);
			st.setStep_no(4);
			st.setStep_DC("국수는 넉넉한 끓는 물에 삶아 찬물에 헹구어 건져 1인분씩 사리지어 놓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(12);
			st.setStep_no(5);
			st.setStep_DC("그릇에 국수를 담고 고기채, 오이채, 동치미무, 계란지단채, 붉은 고추채를 얹어 동치미 국물을 가만히 붓는다");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(13);
			st.setStep_no(1);
			st.setStep_DC("열무는 다듬어 소금에 절여둔다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/002800_p01.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(13);
			st.setStep_no(2);
			st.setStep_DC("냄비에 물 1컵과 찹쌀가루 2큰술을 넣고 나무주걱으로 저어가며 약불에서 찹쌀풀을 쑨다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/002800_p02.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(13);
			st.setStep_no(3);
			st.setStep_DC("볼에 물 4컵과 고춧가루 4큰술을 풀고 찹쌀풀을 넣어 간을 맞춘다.");
			st.setImage_URL(null);

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(13);
			st.setStep_no(4);
			st.setStep_DC("③에 절인 열무와 어슷썬 고추, 채썬 파, 마늘, 생강을 넣고 버무려 열무김치를 담아 익힌다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(13);
			st.setStep_no(5);
			st.setStep_DC("알맞게 익은 열무김치에 식초, 설탕, 소금으로 간을 맞추어 차게 둔다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(13);
			st.setStep_no(6);
			st.setStep_DC("냉면을 삶아 그릇에 담고 열무김치를 부은 다음 겨자 갠 것을 곁들인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(14);
			st.setStep_no(1);
			st.setStep_DC("오이는 두께 0.3cm x 폭 0.3cm, 길이 5cm로 채썰어 소금에 절였다가 물기를 짠다.");
			st.setStep_tip("**쇠고기, 표고버섯 양념장: 간장 2작은술, 설탕 1/2작은술, 다진 파 1/2작은술, 다진 마늘 1/2작은술, 후춧가루 약간, 깨소금 약간, 참기름 약간  **양념간장 : 고춧가루 1큰술, 간장 1큰술, 다진 마늘 1작은술, 다진 파 1작은술, 깨소금 약간, 참기름 약간      * 양념간장");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(14);
			st.setStep_no(2);
			st.setStep_DC("쇠고기는 두께와 폭이 0.3cm, 길이 5cm로 썰어 양념한다.");
			st.setStep_tip(null);

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(14);
			st.setStep_no(3);
			st.setStep_DC("표고도 물에 불려 쇠고기와 같은 크기로 채썰어 양념한다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(14);
			st.setStep_no(4);
			st.setStep_DC("부추는 잘 씻어 5cm 길이로 잘라둔다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(14);
			st.setStep_no(5);
			st.setStep_DC("계란은 노른자와 흰자를 분리하여 지단을 붙이고 채썰어 둔다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(14);
			st.setStep_no(6);
			st.setStep_DC("번철에 오이, 표고, 쇠고기순으로 볶는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(15);
			st.setStep_no(1);
			st.setStep_DC("고기는 저며서 녹말가루, 계란흰자, 청주, 소금으로 양념한다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(15);
			st.setStep_no(2);
			st.setStep_DC("해물은 한 입 크기로 손질해 데친다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(15);
			st.setStep_no(3);
			st.setStep_DC("채소는 얄팍하게 저며썬다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(15);
			st.setStep_no(4);
			st.setStep_DC("팬에 기름을 둘러 다진 마늘을 볶다가 고기를 넣어 볶는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(15);
			st.setStep_no(5);
			st.setStep_DC("어느정도 익으면 멸치국물을 부어 끓이다가 해물과 채소를 넣고 간을 한다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(15);
			st.setStep_no(6);
			st.setStep_DC("삶은 국수에 ⑤의 해물장국을 부어낸다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(16);
			st.setStep_no(1);
			st.setStep_DC("김치는 소를 털고 송송 썰어 물기를 꼭 짜고 숙주는 삶아 물기를 뺀다.");
			st.setStep_tip("*만둣속에 당면이나, 여러가지 채소를 다져 넣어도 색다른 맛을 느낄 수가 있다.      *만둣국의 고명으로 지단을 얇게 부쳐 골패모양으로 썰어 올리면 멋스럽다.      *밀가루 반죽을 한다음에는 바로 빚지 말고 비닐에 조금 숙성시킨 후 만두를 빚는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(16);
			st.setStep_no(2);
			st.setStep_DC("갈은 돼지고기나 갈은 쇠고기를 준비한다.");
			st.setStep_tip(null);

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(16);
			st.setStep_no(3);
			st.setStep_DC("두부는 수분을 완전히 제거한다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(16);
			st.setStep_no(4);
			st.setStep_DC("양파, 마늘, 대파는 곱게 다져놓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(16);
			st.setStep_no(5);
			st.setStep_DC("김치, 숙주, 갈은 고기, 다진 양파, 마늘, 대파에 참기름과 후춧가루, 소금으로 간을 한다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(16);
			st.setStep_no(6);
			st.setStep_DC("밀가루 반죽을 하여 얇게 민다음 지름이 6cm 정도 되게 하여 그 안에 ⑤에서 만든 만둣속을 넣는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(17);
			st.setStep_no(1);
			st.setStep_DC("다시마는 두텁고 광택이 있는 것으로 물에 씻어 젖은 행주로 여러 번 닦은 뒤 찬물 5컵에 담가 30분 정도 불렸다가 건져놓는다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/003900_p01.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(17);
			st.setStep_no(2);
			st.setStep_DC("오이는 소금으로 비벼 씻어 헹궈 가늘게 채썬 뒤 찬물에 잠시 담갔다가 건져놓는다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/003900_p02.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(17);
			st.setStep_no(3);
			st.setStep_DC("홍고추는 반으로 갈라 씨를 털어 곱게 채썬다.");
			st.setImage_URL(null);

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(17);
			st.setStep_no(4);
			st.setStep_DC("손질한 다시마를 돌돌 말아 가늘게 채썰어 달라붙지 않게 털어둔다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(17);
			st.setStep_no(5);
			st.setStep_DC("넓은 그릇에 다시마, 채썬 오이, 홍고추를 담고 다진 파와 마늘, 고춧가루, 깨소금, 소금으로 양념하여 차게 해둔 ①을 붓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(18);
			st.setStep_no(1);
			st.setStep_DC("쇠고기는 얄팍하게 썰어 다진 마늘과 후춧가루, 진간장으로 조물조물 양념한다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(18);
			st.setStep_no(2);
			st.setStep_DC("양념한 고기를 냄비에 넣고 육수를 부어 끓이다가 고추장을 연하게 풀고 국간장이나 소금으로 간을 맞춘다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(18);
			st.setStep_no(3);
			st.setStep_DC("두부를 1cm 폭으로 길쭉하게 썰어 끓고 있는 국물에 쏟아 넣고 두부가 떠오를 때까지 끓인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(18);
			st.setStep_no(4);
			st.setStep_DC("두부가 떠 오르면 채썬 파, 다진 마늘을 넣고 조금 더 끓인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(19);
			st.setStep_no(1);
			st.setStep_DC("모시조개는 소금물로 해감시킨 후 물을 붓고 끓이다가 모시는 건져내고 국물은 걸러 놓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(19);
			st.setStep_no(2);
			st.setStep_DC("콩나물은 다듬어 씻고 두부는 큼직하게 썰며, 붉은 고추는 어슷썰어 씨를 털고 실파는 4cm 길이로 썰어 놓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(19);
			st.setStep_no(3);
			st.setStep_DC("북어포는 물에 씻어 물기를 짠 뒤 소금과 후춧가루로 양념한 뒤 밀가루를 묻혀 계란을 푼 물에 섞는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(19);
			st.setStep_no(4);
			st.setStep_DC("조개 삶아낸 국물에 다진 마늘을 넣고 끓이다가 손질한 콩나물을 넣는다.");

			mStepDao.setInsertStep(st);
			st.setRecipe_ID(19);
			st.setStep_no(5);
			st.setStep_DC("한소끔 끓으면 모시조개 두부, 붉은 고추, 실파를 넣고 계란물에 섞어 놓은 북어를 넣어 조금 더 끓이다가 소금, 후춧가루로 간을 한다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(20);
			st.setStep_no(1);
			st.setStep_DC("쇠고기는 2cm 길이로 도톰하게 저며썰고 무는 길이 3cm 정도씩 토막을 낸 뒤 반을 갈라 0.2cm 두께로 나박썬다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(20);
			st.setStep_no(2);
			st.setStep_DC("썰어놓은 쇠고기는 다진 마늘 1작은술과 국간장 1큰술, 후춧가루 약간으로 양념해 간이 골고루 배도록 조물조물 무친다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(20);
			st.setStep_no(3);
			st.setStep_DC("냄비에 물 5컵을 붓고 팔팔 끓이다가 양념해 놓은 쇠고기를 넣고 고기가 익을 때까지 한소끔 끓인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(20);
			st.setStep_no(4);
			st.setStep_DC("끓는 쇠고기 장국에 나박썬 무를 넣는다. 끓을 때 생기는 거품은 걷어낸다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(20);
			st.setStep_no(5);
			st.setStep_DC("무가 말갛게 익으면 국간장과 소금을 1:1의 비율로 넣어 간을 하고 다진 마늘과 실파를 넣어 조금 더 끓인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(21);
			st.setStep_no(1);
			st.setStep_DC("찬물에 씻어 담가 충분히 불려 물기를 뺀 뒤 손으로 적당하게 잘라 놓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(21);
			st.setStep_no(2);
			st.setStep_DC("쇠고기는 연한 살코기만을 골라 곱게 다져 다진 마늘, 국간장, 참기름을 넣고 간이 배도록 고루 섞는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(21);
			st.setStep_no(3);
			st.setStep_DC("냄비에 식용유를 두르고 양념한 쇠고기를 넣어 볶다가 고기가 익으면 손질한 미역을 넣고 같이 볶는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(21);
			st.setStep_no(4);
			st.setStep_DC("미역이 푸른색을 띠게 볶아지면 분량의 물을 붓고 한소끔 끓인 뒤 다진 마늘을 넣고 국간장, 후춧가루로 간을 한다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(22);
			st.setStep_no(1);
			st.setStep_DC("미역은 줄기를 떼어 티를 골라낸 다음 찬물에 담가30분 정도 불린다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/004800_p01.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(22);
			st.setStep_no(2);
			st.setStep_DC("불린 미역을 깨끗이 씻어 손으로 잘게 찢은 다음 끓는 물에 재빨리 데쳐 물기를 빼놓는다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/004800_p02.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(22);
			st.setStep_no(3);
			st.setStep_DC("오이는 소금에 비벼 씻어 가늘게 채썰어 찬물에 잠깐 담가 건진다.");
			st.setImage_URL(null);

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(22);
			st.setStep_no(4);
			st.setStep_DC("대파 흰부분만 다듬어 씻은 다음 반으로 갈라 가늘게 채썬다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(22);
			st.setStep_no(5);
			st.setStep_DC("큰 그릇에 데친 미역을 담고 분량의 양념 재료를 넣어 무쳐준다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(22);
			st.setStep_no(6);
			st.setStep_DC("양념한 미역에 맛이 배어들면 차갑게 준비한 미역 우린물을 붓고 채썬 오이와 대파를 넣고 차게 먹는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(23);
			st.setStep_no(1);
			st.setStep_DC("생태는 씻어 내장을 빼고 비늘을 긁은 뒤 다시 씻어 5cm 길이로 토막내어 소금을 살짝 뿌려둔다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(23);
			st.setStep_no(2);
			st.setStep_DC("콩나물은 뿌리를 떼고 무는 0.5cm 두께로 얄팍하게 나박썬다. 붉은 고추는 어슷썰어 씨를 털고 미나리는 줄기만 4cm 길이로 썬다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(23);
			st.setStep_no(3);
			st.setStep_DC("마지막에 씻은 쌀뜨물을 받아 냄비에 붓고 센불에서 끓이다가 손질한 생태를 넣는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(23);
			st.setStep_no(4);
			st.setStep_DC("국물이 끓어 생태가 익으면 무와 콩나물을 넣고 뚜껑을 덮은채 한소끔 끓인다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(23);
			st.setStep_no(5);
			st.setStep_DC("거의 끓었을 때 붉은 고추와 다진 마늘, 미나리를 넣고 소금과 후춧가루로 간 한 뒤 더 끓이다가 미나리가 살짝 숨이 죽으면 불에서 내린다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(24);
			st.setStep_no(1);
			st.setStep_DC("[연어준비] 신선한 연어는 깨끗하게 포를 3장 떠놓고 포 위에 레몬즙을 발라 놓는다.(미리 발라두면 색이 변하므로 페스토 소스바르기 직전에 한다)");
			st.setStep_tip("[페스토 소스 재료] 바질 20g, 이태리 파슬리 6줄기, 잣 1/4컵, 엔초비 1마리, 마늘 2쪽, 파르메쟌 10g, 올리브오일 100g");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(24);
			st.setStep_no(2);
			st.setStep_DC("양파는 잘게 다져 놓는다.");
			st.setStep_tip(null);

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(24);
			st.setStep_no(3);
			st.setStep_DC("양송이는 3mm 두께로 슬라이스 한다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(24);
			st.setStep_no(4);
			st.setStep_DC("차이브는 물에 담궈 살려놓는다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(24);
			st.setStep_no(5);
			st.setStep_DC("[페스토소스 만들기] 바질, 이태리파슬리, 잣, 엔초비, 마늘을 믹서에 넣고 올리브오일를 넣어 가며 살짝만 갈아 꺼내고, 올리브오일와 파르메쟌을 넣어 농도와 맛을 낸다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(24);
			st.setStep_no(6);
			st.setStep_DC("[양송이 샐러드 만들기] 양송이는 5mm두께로 썰어 다진 양파, 레몬주스, 올리브오일, 소금, 후춧가루로 맛을 낸다.");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(25);
			st.setStep_no(1);
			st.setStep_DC("오이는 굵은 소금으로 박박 문질러 씻은 뒤 채썰거나 얇게 통썰기 한다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/005200_p01.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(25);
			st.setStep_no(2);
			st.setStep_DC("손질한 오이에 다진 파와 마늘, 고춧가루, 식초를 넣어 골고루 무쳐놓는다.");
			st.setImage_URL("http://file.okdab.com/UserFiles/searching/recipe/005200_p02.jpg");

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(25);
			st.setStep_no(3);
			st.setStep_DC("물 4컵을 끓였다 식힌 뒤 설탕 1큰술과 간장, 식초 2큰술씩, 소금 약간을 넣고 나머지 6컵의 생수를 넣어 냉국물을 만든다.");
			st.setImage_URL(null);

			mStepDao.setInsertStep(st);

			st.setRecipe_ID(25);
			st.setStep_no(4);
			st.setStep_DC("상에 낼 때에는 양념해 놓은 오이에 차갑게 식힌 냉국물을 붓고 깨소금을 살짝 뿌린다.");

			mStepDao.setInsertStep(st);


			// Integredient

			Ingredient in = new Ingredient();

			in.setRecipe_ID(1);
			in.setIrdnt_sn(1);
			in.setName("쌀");
			in.setCapacity(4);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(2);
			in.setName("안심");
			in.setCapacity(200);
			in.setCpciy_unit("g");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(3);
			in.setName("콩나물");
			in.setCapacity(20);
			in.setCpciy_unit("g");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(4);
			in.setName("청포묵");
			in.setCapacity(0.5);
			in.setCpciy_unit("모");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(5);
			in.setName("미나리");
			in.setCapacity(20);
			in.setCpciy_unit("g");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(6);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(7);
			in.setName("국간장");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(8);
			in.setName("다진파");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(9);
			in.setName("다진마늘");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(10);
			in.setName("참기름");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(11);
			in.setName("고추장");
			in.setCapacity(0.5);
			in.setCpciy_unit("큰술");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(12);
			in.setName("설탕");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(13);
			in.setName("숙주");
			in.setCapacity(20);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(14);
			in.setName("도라지");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(15);
			in.setName("고사리");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(16);
			in.setName("계란");
			in.setCapacity(1);
			in.setCpciy_unit("개");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(1);
			in.setIrdnt_sn(17);
			in.setName("양지머리");
			in.setCapacity(100);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(2);
			in.setIrdnt_sn(18);
			in.setName("멥쌀");
			in.setCapacity(1);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(2);
			in.setIrdnt_sn(19);
			in.setName("찹쌀");
			in.setCapacity(2);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(2);
			in.setIrdnt_sn(20);
			in.setName("수수");
			in.setCapacity(1);
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(2);
			in.setIrdnt_sn(21);
			in.setName("차조");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(2);
			in.setIrdnt_sn(22);
			in.setName("콩");
			in.setCapacity(0.5);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(2);
			in.setIrdnt_sn(23);
			in.setName("팥");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(2);
			in.setIrdnt_sn(24);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(25);
			in.setName("당면");
			in.setCapacity(50);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(26);
			in.setName("돼지고기");
			in.setCapacity(100);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(27);
			in.setName("표고버섯");
			in.setCapacity(2);
			in.setCpciy_unit("장");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(28);
			in.setName("호박");
			in.setCapacity(0.25);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(29);
			in.setName("당근");
			in.setCapacity(0.5);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(30);
			in.setName("부추");
			in.setCapacity(30);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(31);
			in.setName("청고추");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(32);
			in.setName("홍고추");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(33);
			in.setName("다진파");
			in.setCapacity(2);
			in.setCpciy_unit("큰술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(34);
			in.setName("다진마늘");
			in.setCapacity(1);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(35);
			in.setName("진간장");
			in.setCapacity(4);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(36);
			in.setName("참기름");
			in.setCapacity(1);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(37);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(38);
			in.setName("밥");
			in.setCapacity(3);
			in.setCpciy_unit("공기");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(3);
			in.setIrdnt_sn(39);
			in.setName("통후추");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(4);
			in.setIrdnt_sn(40);
			in.setName("쌀");
			in.setCapacity(2);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(4);
			in.setIrdnt_sn(41);
			in.setName("콩나물");
			in.setCapacity(300);
			in.setCpciy_unit("g");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(4);
			in.setIrdnt_sn(42);
			in.setName("쇠고기");
			in.setCapacity(100);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(4);
			in.setIrdnt_sn(43);
			in.setName("파");
			in.setCapacity(0.5);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(4);
			in.setIrdnt_sn(44);
			in.setName("마늘");
			in.setCapacity(2);
			in.setCpciy_unit("쪽");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(4);
			in.setIrdnt_sn(45);
			in.setName("참기름");
			in.setCapacity(1);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(4);
			in.setIrdnt_sn(46);
			in.setName("깨소금");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(4);
			in.setIrdnt_sn(47);
			in.setName("고춧가루");
			in.setCapacity(NULL);
			in.setCpciy_unit("조금");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(4);
			in.setIrdnt_sn(48);
			in.setName("진간장");
			in.setCapacity(4);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(4);
			in.setIrdnt_sn(49);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(4);
			in.setIrdnt_sn(50);
			in.setName("통후추");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(5);
			in.setIrdnt_sn(51);
			in.setName("찹쌀");
			in.setCapacity(3);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(5);
			in.setIrdnt_sn(52);
			in.setName("흑설탕");
			in.setCapacity(1);
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(5);
			in.setIrdnt_sn(53);
			in.setName("계핏가루");
			in.setCapacity(0.5);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(5);
			in.setIrdnt_sn(54);
			in.setName("깐밤");
			in.setCapacity(200);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(5);
			in.setIrdnt_sn(55);
			in.setName("대추");
			in.setCapacity(50);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(5);
			in.setIrdnt_sn(56);
			in.setName("잣");
			in.setCapacity(1);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(5);
			in.setIrdnt_sn(57);
			in.setName("물엿");
			in.setCapacity(NULL);
			in.setCpciy_unit("적당량");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(5);
			in.setIrdnt_sn(58);
			in.setName("식물성기름");
			in.setCapacity(3);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(5);
			in.setIrdnt_sn(59);
			in.setName("흰설탕");
			in.setCapacity(1);
			in.setCpciy_unit("컵");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(5);
			in.setIrdnt_sn(60);
			in.setName("간장");
			in.setCapacity(0.33);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(5);
			in.setIrdnt_sn(61);
			in.setName("물");
			in.setCapacity(4);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(6);
			in.setIrdnt_sn(62);
			in.setName("청동호박");
			in.setCapacity(0.5);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(6);
			in.setIrdnt_sn(63);
			in.setName("팥");
			in.setCapacity(1);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(6);
			in.setIrdnt_sn(64);
			in.setName("찹쌀");
			in.setCapacity(2);
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(6);
			in.setIrdnt_sn(65);
			in.setName("물");
			in.setCapacity(10);
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(6);
			in.setIrdnt_sn(66);
			in.setName("설탕");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(6);
			in.setIrdnt_sn(67);
			in.setName("소금");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(7);
			in.setIrdnt_sn(68);
			in.setName("쌀");
			in.setCapacity(1);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(7);
			in.setIrdnt_sn(69);
			in.setName("흑임자");
			in.setCapacity(0.5);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(7);
			in.setIrdnt_sn(70);
			in.setName("물");
			in.setCapacity(6);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(7);
			in.setIrdnt_sn(71);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(7);
			in.setIrdnt_sn(72);
			in.setName("설탕");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(8);
			in.setIrdnt_sn(73);
			in.setName("쇠고기");
			in.setCapacity(200);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(8);
			in.setIrdnt_sn(74);
			in.setName("감자");
			in.setCapacity(2);
			in.setCpciy_unit("개");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(8);
			in.setIrdnt_sn(75);
			in.setName("양파");
			in.setCapacity(1);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(8);
			in.setIrdnt_sn(76);
			in.setName("당근");
			in.setCapacity(0.5);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(8);
			in.setIrdnt_sn(77);
			in.setName("완두콩");
			in.setCapacity(4);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(8);
			in.setIrdnt_sn(78);
			in.setName("카레");
			in.setCapacity(70);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(8);
			in.setIrdnt_sn(79);
			in.setName("우유");
			in.setCapacity(0.5);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(8);
			in.setIrdnt_sn(80);
			in.setName("밥");
			in.setCapacity(4);
			in.setCpciy_unit("공기");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(8);
			in.setIrdnt_sn(81);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(8);
			in.setIrdnt_sn(82);
			in.setName("통후추");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(83);
			in.setName("계란");
			in.setCapacity(3);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(84);
			in.setName("양파");
			in.setCapacity(0.25);
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(85);
			in.setName("물");
			in.setCapacity(3);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(86);
			in.setName("당근");
			in.setCapacity(0.33);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(87);
			in.setName("쌈장");
			in.setCapacity(3);
			in.setCpciy_unit("큰술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(88);
			in.setName("후춧가루");
			in.setCapacity(NULL);
			in.setCpciy_unit("\b약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(89);
			in.setName("소금");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(90);
			in.setName("밥");
			in.setCapacity(2);
			in.setCpciy_unit("공기");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(91);
			in.setName("청피망");
			in.setCapacity(0.5);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(92);
			in.setName("홍피망");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(93);
			in.setName("오이");
			in.setCapacity(0.33);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(94);
			in.setName("전분");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(95);
			in.setName("멸치");
			in.setCapacity(1);
			in.setCpciy_unit("줌");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(9);
			in.setIrdnt_sn(96);
			in.setName("다시마");
			in.setCpciy_unit("장");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(10);
			in.setIrdnt_sn(97);
			in.setName("밀가루");
			in.setCapacity(4);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(10);
			in.setIrdnt_sn(98);
			in.setName("감자");
			in.setCapacity(2);
			in.setCpciy_unit("개");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(10);
			in.setIrdnt_sn(99);
			in.setName("애호박");
			in.setCapacity(0.5);
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(10);
			in.setIrdnt_sn(100);
			in.setName("멸치");
			in.setCapacity(10);
			in.setCpciy_unit("마리");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(10);
			in.setIrdnt_sn(101);
			in.setName("실파");
			in.setCapacity(2);
			in.setCpciy_unit("뿌리");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(10);
			in.setIrdnt_sn(102);
			in.setName("홍고추");
			in.setCapacity(1);
			in.setCpciy_unit("개");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(10);
			in.setIrdnt_sn(103);
			in.setName("물");
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(10);
			in.setIrdnt_sn(104);
			in.setName("양념장");
			in.setCapacity(NULL);
			in.setCpciy_unit("적당량");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(10);
			in.setIrdnt_sn(105);
			in.setName("국간장");
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(10);
			in.setIrdnt_sn(106);
			in.setName("참기름");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(10);
			in.setIrdnt_sn(107);
			in.setName("소금");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(11);
			in.setIrdnt_sn(108);
			in.setName("냉면");
			in.setCapacity(400);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(11);
			in.setIrdnt_sn(109);
			in.setName("쇠고기");
			in.setCapacity(300);
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(11);
			in.setIrdnt_sn(110);
			in.setName("동치미국물");
			in.setCapacity(1);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(11);
			in.setIrdnt_sn(111);
			in.setName("오이");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(11);
			in.setIrdnt_sn(112);
			in.setName("배");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(11);
			in.setIrdnt_sn(113);
			in.setName("계란");
			in.setCapacity(2);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(11);
			in.setIrdnt_sn(114);
			in.setName("식초");
			in.setCpciy_unit("큰술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(11);
			in.setIrdnt_sn(115);
			in.setName("설탕");
			in.setCapacity(1);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(11);
			in.setIrdnt_sn(116);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(11);
			in.setIrdnt_sn(117);
			in.setName("쇠고기육수");
			in.setCapacity(4);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(11);
			in.setIrdnt_sn(118);
			in.setName("동치미무");
			in.setCapacity(1);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(12);
			in.setIrdnt_sn(119);
			in.setName("국수");
			in.setCapacity(400);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(12);
			in.setIrdnt_sn(120);
			in.setName("쇠고기");
			in.setCapacity(100);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(12);
			in.setIrdnt_sn(121);
			in.setName("동치미무");
			in.setCapacity(0.25);
			in.setCpciy_unit("개");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(12);
			in.setIrdnt_sn(122);
			in.setName("계란");
			in.setCapacity(1);
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(12);
			in.setIrdnt_sn(123);
			in.setName("홍고추");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(12);
			in.setIrdnt_sn(124);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(12);
			in.setIrdnt_sn(125);
			in.setName("깨소금");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(12);
			in.setIrdnt_sn(126);
			in.setName("참기름");
			in.setCapacity(0.5);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(12);
			in.setIrdnt_sn(127);
			in.setName("동치미국물");
			in.setCapacity(8);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(12);
			in.setIrdnt_sn(128);
			in.setName("간장");
			in.setCapacity(1);
			in.setCpciy_unit("큰술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(12);
			in.setIrdnt_sn(129);
			in.setName("설탕");
			in.setCapacity(0.25);
			in.setCpciy_unit("작은술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(12);
			in.setIrdnt_sn(130);
			in.setName("통후추");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(13);
			in.setIrdnt_sn(131);
			in.setName("열무");
			in.setCapacity(0.5);
			in.setCpciy_unit("단");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(13);
			in.setIrdnt_sn(132);
			in.setName("찹쌀가루");
			in.setCapacity(2);
			in.setCpciy_unit("큰술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(13);
			in.setIrdnt_sn(133);
			in.setName("냉면");
			in.setCapacity(500);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(13);
			in.setIrdnt_sn(134);
			in.setName("홍고추");
			in.setCapacity(1);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(13);
			in.setIrdnt_sn(135);
			in.setName("청고추");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(13);
			in.setIrdnt_sn(136);
			in.setName("대파");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(13);
			in.setIrdnt_sn(137);
			in.setName("다진마늘");
			in.setCapacity(1);
			in.setCpciy_unit("작은술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(13);
			in.setIrdnt_sn(138);
			in.setName("다진생강");
			in.setCapacity(0.5);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(13);
			in.setIrdnt_sn(139);
			in.setName("고춧가루");
			in.setCapacity(4);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);


			in.setRecipe_ID(13);
			in.setIrdnt_sn(140);
			in.setName("식초");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(13);
			in.setIrdnt_sn(141);
			in.setName("설탕");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(13);
			in.setIrdnt_sn(142);
			in.setName("겨자");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(13);
			in.setIrdnt_sn(143);
			in.setName("물");
			in.setCapacity(1);
			in.setCpciy_unit("컵");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(14);
			in.setIrdnt_sn(144);
			in.setName("국수");
			in.setCapacity(200);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(14);
			in.setIrdnt_sn(145);
			in.setName("표고버섯");
			in.setCapacity(3);
			in.setCpciy_unit("장");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(14);
			in.setIrdnt_sn(146);
			in.setName("오이");
			in.setCapacity(0.5);
			in.setCpciy_unit("개");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(14);
			in.setIrdnt_sn(147);
			in.setName("계란");
			in.setCapacity(1);
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(14);
			in.setIrdnt_sn(148);
			in.setName("부추");
			in.setCapacity(0.33);
			in.setCpciy_unit("단");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(14);
			in.setIrdnt_sn(149);
			in.setName("쇠고기");
			in.setCapacity(100);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(14);
			in.setIrdnt_sn(150);
			in.setName("양념장");
			in.setCapacity(NULL);
			in.setCpciy_unit("적당량");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(14);
			in.setIrdnt_sn(151);
			in.setName("상추");
			in.setCapacity(6);
			in.setCpciy_unit("장");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(14);
			in.setIrdnt_sn(152);
			in.setName("깻잎");
			in.setCapacity(15);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(153);
			in.setName("국수");
			in.setCapacity(400);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(154);
			in.setName("돼지고기");
			in.setCapacity(100);
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(155);
			in.setName("녹말");
			in.setCapacity(1);
			in.setCpciy_unit("큰술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(156);
			in.setName("계란흰자");
			in.setCpciy_unit("개");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(157);
			in.setName("오징어");
			in.setCapacity(0.5);
			in.setCpciy_unit("마리");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(158);
			in.setName("새우");
			in.setCapacity(100);
			in.setCpciy_unit("g");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(159);
			in.setName("홍합");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(160);
			in.setName("표고버섯");
			in.setCapacity(2);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(161);
			in.setName("죽순");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(162);
			in.setName("파");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(163);
			in.setName("다진마늘");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(164);
			in.setName("멸칫국물");
			in.setCapacity(8);
			in.setCpciy_unit("컵");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(165);
			in.setName("청주");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(15);
			in.setIrdnt_sn(166);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(167);
			in.setName("김치");
			in.setCapacity(0.5);
			in.setCpciy_unit("포기");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(168);
			in.setName("숙주");
			in.setCapacity(150);
			in.setCpciy_unit("g");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(169);
			in.setName("표고버섯");
			in.setCapacity(4);
			in.setCpciy_unit("장");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(170);
			in.setName("두부");
			in.setCapacity(0.5);
			in.setCpciy_unit("모");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(171);
			in.setName("만두피");
			in.setCapacity(40);
			in.setCpciy_unit("장");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(172);
			in.setName("육수");
			in.setCapacity(7);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(173);
			in.setName("쇠고기");
			in.setCapacity(40);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(174);
			in.setName("다진파");
			in.setCapacity(1);
			in.setCpciy_unit("큰술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(175);
			in.setName("다진마늘");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(176);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(177);
			in.setName("후춧가루");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(178);
			in.setName("깨소금");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(179);
			in.setName("간장");
			in.setCapacity(1);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(180);
			in.setName("설탕");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(16);
			in.setIrdnt_sn(181);
			in.setName("참기름");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(17);
			in.setIrdnt_sn(182);
			in.setName("다시마");
			in.setCapacity(2);
			in.setCpciy_unit("장");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(17);
			in.setIrdnt_sn(183);
			in.setName("오이");
			in.setCapacity(0.5);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(17);
			in.setIrdnt_sn(184);
			in.setName("홍고추");
			in.setCapacity(1);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(17);
			in.setIrdnt_sn(185);
			in.setName("다진파");
			in.setCapacity(0.5);
			in.setCpciy_unit("작은술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(17);
			in.setIrdnt_sn(186);
			in.setName("다진마늘");
			in.setCapacity(0.25);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(17);
			in.setIrdnt_sn(187);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(17);
			in.setIrdnt_sn(188);
			in.setName("고춧가루");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(17);
			in.setIrdnt_sn(189);
			in.setName("깨소금");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(17);
			in.setIrdnt_sn(190);
			in.setName("물");
			in.setCapacity(5);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(18);
			in.setIrdnt_sn(191);
			in.setName("두부");
			in.setCapacity(1);
			in.setCpciy_unit("모");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(18);
			in.setIrdnt_sn(192);
			in.setName("쇠고기");
			in.setCapacity(100);
			in.setCpciy_unit("g");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(18);
			in.setIrdnt_sn(193);
			in.setName("파");
			in.setCapacity(1);
			in.setCpciy_unit("뿌리");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(18);
			in.setIrdnt_sn(194);
			in.setName("다진마늘");
			in.setCpciy_unit("큰술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(18);
			in.setIrdnt_sn(195);
			in.setName("고추장");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(18);
			in.setIrdnt_sn(196);
			in.setName("참기름");
			in.setCpciy_unit("작은술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(18);
			in.setIrdnt_sn(197);
			in.setName("진간장");
			in.setCapacity(NULL);
			in.setCpciy_unit("조금");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(18);
			in.setIrdnt_sn(198);
			in.setName("소금");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(18);
			in.setIrdnt_sn(199);
			in.setName("후춧가루");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(19);
			in.setIrdnt_sn(200);
			in.setName("두부");
			in.setCapacity(0.5);
			in.setCpciy_unit("모");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(19);
			in.setIrdnt_sn(201);
			in.setName("콩나물");
			in.setCapacity(50);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(19);
			in.setIrdnt_sn(202);
			in.setName("모시조개");
			in.setCapacity(200);
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(19);
			in.setIrdnt_sn(203);
			in.setName("북어");
			in.setCapacity(30);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(19);
			in.setIrdnt_sn(204);
			in.setName("홍고추");
			in.setCapacity(1);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(19);
			in.setIrdnt_sn(205);
			in.setName("실파");
			in.setCapacity(3);
			in.setCpciy_unit("뿌리");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(19);
			in.setIrdnt_sn(206);
			in.setName("밀가루");
			in.setCapacity(1);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(19);
			in.setIrdnt_sn(207);
			in.setName("계란");
			in.setCpciy_unit("개");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(19);
			in.setIrdnt_sn(208);
			in.setName("마늘");
			in.setCpciy_unit("큰술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(19);
			in.setIrdnt_sn(209);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(19);
			in.setIrdnt_sn(210);
			in.setName("후춧가루");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(20);
			in.setIrdnt_sn(211);
			in.setName("쇠고기");
			in.setCapacity(120);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(20);
			in.setIrdnt_sn(212);
			in.setName("무");
			in.setCapacity(400);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(20);
			in.setIrdnt_sn(213);
			in.setName("실파");
			in.setCapacity(20);
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(20);
			in.setIrdnt_sn(214);
			in.setName("마늘");
			in.setCapacity(1);
			in.setCpciy_unit("작은술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(20);
			in.setIrdnt_sn(215);
			in.setName("국간장");
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(20);
			in.setIrdnt_sn(216);
			in.setName("소금");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(20);
			in.setIrdnt_sn(217);
			in.setName("참기름");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(20);
			in.setIrdnt_sn(218);
			in.setName("후춧가루");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(20);
			in.setIrdnt_sn(219);
			in.setName("물");
			in.setCapacity(5);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(21);
			in.setIrdnt_sn(220);
			in.setName("미역");
			in.setCapacity(2);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(21);
			in.setIrdnt_sn(221);
			in.setName("다진쇠고기");
			in.setCapacity(150);
			in.setCpciy_unit("g");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(21);
			in.setIrdnt_sn(222);
			in.setName("마늘");
			in.setCapacity(1);
			in.setCpciy_unit("큰술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(21);
			in.setIrdnt_sn(223);
			in.setName("식용유");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(21);
			in.setIrdnt_sn(224);
			in.setName("후춧가루");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(21);
			in.setIrdnt_sn(225);
			in.setName("국간장");
			in.setCapacity(2);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(21);
			in.setIrdnt_sn(226);
			in.setName("참기름");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(21);
			in.setIrdnt_sn(227);
			in.setName("물");
			in.setCapacity(10);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(22);
			in.setIrdnt_sn(228);
			in.setName("불린미역");
			in.setCapacity(2);
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(22);
			in.setIrdnt_sn(229);
			in.setName("오이");
			in.setCapacity(0.5);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(22);
			in.setIrdnt_sn(230);
			in.setName("대파");
			in.setCapacity(0.5);
			in.setCpciy_unit("뿌리");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(22);
			in.setIrdnt_sn(231);
			in.setName("다진마늘");
			in.setCpciy_unit("작은술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(22);
			in.setIrdnt_sn(232);
			in.setName("깨소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(22);
			in.setIrdnt_sn(233);
			in.setName("소금");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(22);
			in.setIrdnt_sn(234);
			in.setName("고춧가루");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(22);
			in.setIrdnt_sn(235);
			in.setName("간장");
			in.setCapacity(2);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(22);
			in.setIrdnt_sn(236);
			in.setName("물");
			in.setCapacity(10);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(23);
			in.setIrdnt_sn(237);
			in.setName("생태");
			in.setCapacity(1);
			in.setCpciy_unit("마리");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(23);
			in.setIrdnt_sn(238);
			in.setName("미나리");
			in.setCapacity(50);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(23);
			in.setIrdnt_sn(239);
			in.setName("콩나물");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(23);
			in.setIrdnt_sn(240);
			in.setName("무");
			in.setCapacity(0.25);
			in.setCpciy_unit("개");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(23);
			in.setIrdnt_sn(241);
			in.setName("홍고추");
			in.setCapacity(1);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(23);
			in.setIrdnt_sn(242);
			in.setName("마늘");
			in.setCapacity(2);
			in.setCpciy_unit("쪽");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(23);
			in.setIrdnt_sn(243);
			in.setName("쌀뜨물");
			in.setCapacity(10);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(23);
			in.setIrdnt_sn(244);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(23);
			in.setIrdnt_sn(245);
			in.setName("후춧가루");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(24);
			in.setIrdnt_sn(246);
			in.setName("연어");
			in.setCapacity(6);
			in.setCpciy_unit("조각");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(24);
			in.setIrdnt_sn(247);
			in.setName("후춧가루");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(24);
			in.setIrdnt_sn(248);
			in.setName("레몬즙");
			in.setCapacity(40);
			in.setCpciy_unit("g");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(24);
			in.setIrdnt_sn(249);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(24);
			in.setIrdnt_sn(250);
			in.setName("양파");
			in.setCapacity(0.5);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(24);
			in.setIrdnt_sn(251);
			in.setName("양송이버섯");
			in.setCapacity(4);

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(24);
			in.setIrdnt_sn(252);
			in.setName("올리브오일");
			in.setCapacity(60);
			in.setCpciy_unit("g");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(24);
			in.setIrdnt_sn(253);
			in.setName("방울토마토");
			in.setCapacity(4);
			in.setCpciy_unit("개");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(24);
			in.setIrdnt_sn(254);
			in.setName("페스토소스");
			in.setCapacity(NULL);
			in.setCpciy_unit("만드는 법 참조");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(24);
			in.setIrdnt_sn(255);
			in.setName("모듬채소");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(24);
			in.setIrdnt_sn(256);
			in.setName("차이브");
			in.setCapacity(4);
			in.setCpciy_unit("개");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(25);
			in.setIrdnt_sn(257);
			in.setName("오이");
			in.setCapacity(2);
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(25);
			in.setIrdnt_sn(258);
			in.setName("파");
			in.setCapacity(1);
			in.setCpciy_unit("작은술");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(25);
			in.setIrdnt_sn(259);
			in.setName("다진마늘");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(25);
			in.setIrdnt_sn(260);
			in.setName("간장");
			in.setCapacity(3);
			in.setCpciy_unit("큰술");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(25);
			in.setIrdnt_sn(261);
			in.setName("식초");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(25);
			in.setIrdnt_sn(262);
			in.setName("설탕");
			in.setCapacity(1);

			mIngredientDao.setInsertIngredient(in);
			in.setIrdnt_sn(263);
			in.setRecipe_ID(25);
			in.setName("깨소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(25);
			in.setIrdnt_sn(264);
			in.setName("고춧가루");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(25);
			in.setIrdnt_sn(265);
			in.setName("물");
			in.setCapacity(10);
			in.setCpciy_unit("컵");
			in.setIrdnt_TY_code(3060001);
			in.setIrdnt_TY_name("주재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(25);
			in.setIrdnt_sn(266);
			in.setName("대파");
			in.setCapacity(1);
			in.setCpciy_unit("대");
			in.setIrdnt_TY_code(3060002);
			in.setIrdnt_TY_name("부재료");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(25);
			in.setIrdnt_sn(267);
			in.setName("홍고추");
			in.setCpciy_unit("개");

			mIngredientDao.setInsertIngredient(in);

			in.setRecipe_ID(25);
			in.setIrdnt_sn(268);
			in.setName("소금");
			in.setCapacity(NULL);
			in.setCpciy_unit("약간");
			in.setIrdnt_TY_code(3060003);
			in.setIrdnt_TY_name("양념");

			mIngredientDao.setInsertIngredient(in);

			User user = new User();

			user.setId("abc1");
			user.setPassword("1111");
			user.setName("Kim");
			user.setBeginner(false);
			mUserDao.setInsertUser(user);

			user.setId("abc2");
			user.setPassword("1234");
			user.setName("Lee");
			mUserDao.setInsertUser(user);

			user.setId("abc3");
			user.setPassword("2222");
			user.setName("Cho");
			mUserDao.setInsertUser(user);

			user.setId("abc4");
			user.setPassword("2345");
			user.setName("Tom");
			mUserDao.setInsertUser(user);

			user.setId("abc5");
			user.setPassword("3333");
			user.setName("Park");
			mUserDao.setInsertUser(user);

			user.setId("abc6");
			user.setPassword("3456");
			user.setName("Son");
			mUserDao.setInsertUser(user);

			user.setId("abc7");
			user.setPassword("4444");
			user.setName("Ahn");
			mUserDao.setInsertUser(user);

			user.setId("abc8");
			user.setPassword("4567");
			user.setName("Kang");
			mUserDao.setInsertUser(user);

			user.setId("abc9");
			user.setPassword("5555");
			user.setName("Shin");
			mUserDao.setInsertUser(user);

			user.setId("abc10");
			user.setPassword("5678");
			user.setName("Chung");
			mUserDao.setInsertUser(user);

			Bookmark mark = new Bookmark();

			mark.setUser_ID("abc9");
			mark.setRecipe_ID(3);
			mBookmarkDao.setInsertBookmark(mark);

			mark.setUser_ID("abc9");
			mark.setRecipe_ID(20);
			mBookmarkDao.setInsertBookmark(mark);

			mark.setUser_ID("abc2");
			mark.setRecipe_ID(3);
			mBookmarkDao.setInsertBookmark(mark);

			mark.setUser_ID("abc7");
			mark.setRecipe_ID(10);
			mBookmarkDao.setInsertBookmark(mark);

			mark.setUser_ID("abc1");
			mark.setRecipe_ID(8);
			mBookmarkDao.setInsertBookmark(mark);


			Dontwant dontwant = new Dontwant();

			dontwant.setId("abc1");
			dontwant.setIrdnt_nm("potato");
			mDontwantDao.setInsertDontwant(dontwant);

			dontwant.setId("abc1");
			dontwant.setIrdnt_nm("tomato");
			mDontwantDao.setInsertDontwant(dontwant);

			dontwant.setId("abc2");
			dontwant.setIrdnt_nm("carrot");
			mDontwantDao.setInsertDontwant(dontwant);

			dontwant.setId("abc3");
			dontwant.setIrdnt_nm("potato");
			mDontwantDao.setInsertDontwant(dontwant);

			dontwant.setId("abc3");
			dontwant.setIrdnt_nm("carrot");
			mDontwantDao.setInsertDontwant(dontwant);

			dontwant.setId("abc3");
			dontwant.setIrdnt_nm("something");
			mDontwantDao.setInsertDontwant(dontwant);

			dontwant.setId("abc4");
			dontwant.setIrdnt_nm("tomato");
			mDontwantDao.setInsertDontwant(dontwant);

			dontwant.setId("abc4");
			dontwant.setIrdnt_nm("something");
			mDontwantDao.setInsertDontwant(dontwant);

		}
}
	
	