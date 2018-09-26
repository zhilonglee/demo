package demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Resolve {

	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub
		Long long1 = 1L;
		System.out.println("sss "+long1);
		String json="[{\"desc\":\"2018年09月14日，山东省青岛市。\",\"pvnum\":\"\",\"createdate\":\"2018-09-20 12:08:40\",\"scover\":\"http://pic-bucket.nosdn.127.net/photo/0001/2018-09-20/DS571FDF00AP0001NOS.jpg?imageView&thumbnail=100y75\",\"setname\":\"男子14年收藏万枚古钱币 自开古钱博物馆\",\"cover\":\"http://pic-bucket.nosdn.127.net/photo/0001/2018-09-20/DS571FDF00AP0001NOS.jpg\",\"pics\":[],\"clientcover1\":\"\",\"replynum\":\"1\",\"topicname\":\"\",\"setid\":\"2296475\",\"seturl\":\"http://news.163.com/photoview/00AP0001/2296475.html\",\"datetime\":\"2018-09-20 12:11:16\",\"clientcover\":\"\",\"imgsum\":\"4\",\"tcover\":\"http://pic-bucket.nosdn.127.net/photo/0001/2018-09-20/DS571FDF00AP0001NOS.jpg?imageView&thumbnail=160y120\"}]";
		
			
			JSONArray jsonArray =new JSONArray(json);
			
			JSONObject jsonObject =jsonArray.getJSONObject(0);
			News news = new News();
			news.setDesc(jsonObject.getString("desc"));
			System.out.println(news.getDesc());
			
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2018-09-20 18:39:03");
				System.err.println(date.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}
}
