package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myactivities.AboutActivity;
import com.myactivities.CollectionActivity;
import com.myactivities.NewsWebView;
import com.myactivities.R;

import java.util.ArrayList;

import Adapters.CreateList;
import Adapters.MyAdapter;
import listeners.MyRecyclerItemClickListener;

public class Home extends Fragment {
    private final String image_titles[] = {
            "Collections",
            "News Pot",
            "About EasymApp",
    };

    private final Integer image_ids[] = {
            R.mipmap.col3,
            R.mipmap.news,
            R.mipmap.aboutus,
    };
    public Home() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_farmingtab, container, false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.livestock_gallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<CreateList> createLists = prepareData();
        MyAdapter adapter = new MyAdapter(getContext(), createLists);
        recyclerView.setAdapter(adapter);

       recyclerView.addOnItemTouchListener(
               (RecyclerView.OnItemTouchListener) new MyRecyclerItemClickListener(getContext(), new MyRecyclerItemClickListener.OnItemClickListener() {
                   @Override
                   public void onItemClick(View view, int position) {
                       if(position==0){
                          Intent intent_col = new Intent(getContext(), CollectionActivity.class);
                           startActivity(intent_col);
                       }/*else if(position==1){
                           Fragment fragment= new AllFarmers();
                           FragmentTransaction transaction = getFragmentManager().beginTransaction();
                           transaction.replace(R.id.content_main, fragment); // fragmen container id in first parameter is the  container(Main layout id) of Activity
                           transaction.addToBackStack(null);  // this will manage backstack
                           transaction.commit();
                       }else if(position==2){
                           Toast.makeText(getContext(), "No Payment Details Fount", Toast.LENGTH_SHORT).show();
                       }*/else if(position==1){
                           Intent intent_news = new Intent(getContext(), NewsWebView.class);
                           startActivity(intent_news);
                       }else if(position==2){
                           Intent intent_abt = new Intent(getContext(), AboutActivity.class);
                           startActivity(intent_abt);
                       }
                   }

               })
       );



        return view;
    }
    private ArrayList<CreateList> prepareData(){

        ArrayList<CreateList> theimage = new ArrayList<>();
        for(int i = 0; i< image_titles.length; i++){
            CreateList createList = new CreateList();
            createList.setImage_title(image_titles[i]);
            createList.setImage_ID(image_ids[i]);
            theimage.add(createList);
        }
        return theimage;
    }
}
