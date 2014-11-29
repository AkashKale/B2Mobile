package com.bbarters.bbartersmobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizActivity extends ActionBarActivity {

	String URL=Constants.getUrl();
	Timer timer;
	TimerTask timerTask;
	TextView tvRemTime;
	int remTime;
	int quesNum=0;
	int totalQues=0;
	int correctAnswers=0;
	int quizid;
	TextView tvQuestion;
	TextView tvOp1;
	TextView tvOp2;
	TextView tvOp3;
	TextView tvOp4;
	ArrayList<String> question=new ArrayList<String>();
	ArrayList<String> op1=new ArrayList<String>();
	ArrayList<String> op2=new ArrayList<String>();
	ArrayList<String> op3=new ArrayList<String>();
	ArrayList<String> op4=new ArrayList<String>();
	ArrayList<Integer> totalOptions=new ArrayList<Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_activity);
		tvRemTime=(TextView)findViewById(R.id.remaining_time);
		tvQuestion=(TextView)findViewById(R.id.question);
		tvOp1=(TextView)findViewById(R.id.op1);
		tvOp2=(TextView)findViewById(R.id.op2);
		tvOp3=(TextView)findViewById(R.id.op3);
		tvOp4=(TextView)findViewById(R.id.op4);
		Button btnSubmit=(Button)findViewById(R.id.submit_quiz_answer);
		final ArrayList<String> correct1=new ArrayList<String>();
		final ArrayList<String> correct2=new ArrayList<String>();
		final ArrayList<String> correct3=new ArrayList<String>();
		final ArrayList<String> correct4=new ArrayList<String>();
		/*final ArrayList<String> question=new ArrayList<String>();
		final ArrayList<String> op1=new ArrayList<String>();
		final ArrayList<String> op2=new ArrayList<String>();
		final ArrayList<String> op3=new ArrayList<String>();
		final ArrayList<String> op4=new ArrayList<String>();
		final ArrayList<Integer> totalOptions=new ArrayList<Integer>();*/
		
		AQuery aq=new AQuery(getApplicationContext());
	   	String url=URL+"mobile_quiz";
	   	Map<String, Object> params = new HashMap<String, Object>();
	   	params.put("contentid",getIntent().getExtras().getInt("contentid"));    
	   	Log.e("quiz sending",getIntent().getExtras().getInt("contentid")+"");
    	aq.ajax(url,params,JSONObject.class, new AjaxCallback<JSONObject>() 
    	{
    		@Override
      		public void callback(String url, JSONObject content, AjaxStatus status) 
      		{
      			Log.e("quiz",content.toString());
      			try {
					JSONArray jsArray=new JSONArray(content.optString("questions"));
					totalQues=jsArray.length();
					remTime=getIntent().getExtras().getInt("time");
					
					for(int i=0;i<jsArray.length();i++)
					{
						JSONObject obj=(JSONObject)jsArray.get(i);
						quizid=obj.optInt("quizid");
						question.add(obj.optString("question"));
						op1.add(obj.optString("option1"));
						op2.add(obj.optString("option2"));
						if(obj.optString("op3").equals(null))
						{
							op3.add("NULL");
							op4.add("NULL");
							totalOptions.add(2);
						}
						else
						{
							op3.add(obj.optString("option3"));
							op4.add(obj.optString("option4"));
							totalOptions.add(4);
						}						
						correct1.add(obj.optString("correct1"));
						correct2.add(obj.optString("correct2"));
						if(obj.optString("correct3").equals(null))
						{
							correct3.add("NULL");
							correct4.add("NULL");
						}
						else
						{
							correct3.add(obj.optString("correct3"));
							correct4.add(obj.optString("correct4"));
						}
					}
					nextQues();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      		}
      	});
    	View.OnClickListener tvSelected=new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView tempTv=(TextView) v;
				if(tempTv.isSelected())
				{
					tempTv.setBackgroundColor(Color.TRANSPARENT);
					tempTv.setSelected(false);
				}
				else
				{
					tempTv.setBackgroundColor(Color.YELLOW);
					tempTv.setSelected(true);
				}
			}
		};
			tvOp1.setOnClickListener(tvSelected);
			tvOp2.setOnClickListener(tvSelected);
			tvOp3.setOnClickListener(tvSelected);
			tvOp4.setOnClickListener(tvSelected);
    	   	btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(((correct1.get(quesNum).equals(1)&&tvOp1.isSelected())||(correct1.get(quesNum).equals(0)&&!tvOp1.isSelected()))
				&&((correct2.get(quesNum).equals(1)&&tvOp2.isSelected())||(correct2.get(quesNum).equals(0)&&!tvOp2.isSelected()))
				&&((correct3.get(quesNum).equals(1)&&tvOp3.isSelected())||(correct3.get(quesNum).equals(0)&&!tvOp3.isSelected()))
				&&((correct4.get(quesNum).equals(1)&&tvOp4.isSelected())||(correct4.get(quesNum).equals(0)&&!tvOp4.isSelected())))
				{
					correctAnswers++;
				}
				quesNum++;
				if(quesNum<totalQues)
					nextQues();
				else
					endQuiz();
			}
		});
	}
	
	protected void nextQues() {
		// TODO Auto-generated method stub
		tvQuestion.setText(question.get(quesNum));
		tvOp1.setText(op1.get(quesNum));
		tvOp1.setBackgroundColor(Color.TRANSPARENT);
		tvOp2.setText(op2.get(quesNum));
		tvOp2.setBackgroundColor(Color.TRANSPARENT);
		if(totalOptions.get(quesNum).equals(4))
		{
			tvOp3.setText(op3.get(quesNum));			
			tvOp4.setText(op4.get(quesNum));
		}
		else
		{
			tvOp3.setVisibility(View.INVISIBLE);
			tvOp4.setVisibility(View.INVISIBLE);
		}
		tvOp3.setBackgroundColor(Color.TRANSPARENT);
		tvOp4.setBackgroundColor(Color.TRANSPARENT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void startTimer()
	{
		stopTimer();
		timer=new Timer();
		timerTask=new TimerTask(){
		  	   public void run()
		  	   {
		    	  // do your code here,, in this example  the code written here will execute at every one second;
		  		   remTime--;
		  		   if(remTime==0)
		    	   {
		    		   stopTimer();
		   			   endQuiz();
		   		   }
		   		   else
		   		   {
	    			   tvRemTime.setText(remTime);
	    		   }
		    	}
		 };
		 timer.scheduleAtFixedRate(timerTask, 1000, 1000);
	   }
	
	   protected void endQuiz() {
		// TODO Auto-generated method stub
		   AQuery aq=new AQuery(getApplicationContext());
		   String url=URL+"mobile_quizResult";
		   Map<String, Object> params = new HashMap<String, Object>();
		   SharedPreferences auth = getSharedPreferences("Auth", Context.MODE_PRIVATE);
		   params.put("id",auth.getInt("id", 0));
		   params.put("correct",correctAnswers);
		   params.put("quizid",quizid);
	       aq.ajax(url,params,JSONArray.class, new AjaxCallback<JSONArray>() 
	       {	       			
	    	   @Override
	       		public void callback(String url, JSONArray res, AjaxStatus status) 
	       		{
	       			Log.e("Quiz result",res.toString());
	       			AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
	       			builder.setMessage("Your current IFCs : ");
	       			
	       			
	       			
	       			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
	       			{
	       				public void onClick(DialogInterface dialog, int id) 
	       				{
	       		   		       finish();
	       			     }
	       			});
	       		}
	       	});
	}
	public void stopTimer()
	{
		if(timer!=null||timerTask!=null)
		{
			if(timer!=null)
				timer.cancel();
			if(timerTask!=null)
				timerTask.cancel();
		}  
		timer=null;
		timerTask=null;
	}
}