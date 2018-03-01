package com.momentu.momentuandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.momentu.momentuandroid.Adapter.MyPostsAdapter;
import com.momentu.momentuandroid.Model.MyPostsItem;

import java.util.ArrayList;
import java.util.List;

public class PostsOfYouActivity extends AppCompatActivity {

    public String token;

    List<MyPostsItem> allItems;

    MyPostsAdapter myPostsAdapter;

    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_of_you);

        token = getIntent().getStringExtra("token");

        gridview = (GridView) findViewById(R.id.gridviewMyPosts);

        allItems = getAllItemObject();
        myPostsAdapter = new MyPostsAdapter(PostsOfYouActivity.this, allItems);
        gridview.setAdapter(myPostsAdapter);

    }


    private List<MyPostsItem> getAllItemObject(){
        MyPostsItem mypost = null;
        List<MyPostsItem> items = new ArrayList<>();
        items.add(new MyPostsItem(0,"#Halloween",
                "http://kb4images.com/images/image/37185176-image.jpg",
                "http://kb4images.com/images/image/37185176-image.jpg","image",1000,
                "Chicago","IL"));

        items.add(new MyPostsItem(0,"#Halloween",
                "http://kb4images.com/images/image/37185176-image.jpg",
                "http://kb4images.com/images/image/37185176-image.jpg","image",1000,
                "Chicago","IL"));

        items.add(new MyPostsItem(0,"#Halloween",
                "http://kb4images.com/images/image/37185176-image.jpg",
                "http://kb4images.com/images/image/37185176-image.jpg","image",1000,
                "Chicago","IL"));

        items.add(new MyPostsItem(0,"#Halloween",
                "http://kb4images.com/images/image/37185176-image.jpg",
                "http://kb4images.com/images/image/37185176-image.jpg","image",1000,
                "Chicago","IL"));

        items.add(new MyPostsItem(0,"#Halloween",
                "http://kb4images.com/images/image/37185176-image.jpg",
                "http://kb4images.com/images/image/37185176-image.jpg","image",1000,
                "Chicago","IL"));

        items.add(new MyPostsItem(0,"#Halloween",
                "http://kb4images.com/images/image/37185176-image.jpg",
                "http://kb4images.com/images/image/37185176-image.jpg","image",1000,
                "Chicago","IL"));

        items.add(new MyPostsItem(0,"#Halloween",
                "http://kb4images.com/images/image/37185176-image.jpg",
                "http://kb4images.com/images/image/37185176-image.jpg","image",1000,
                "Chicago","IL"));

        items.add(new MyPostsItem(0,"#Halloween",
                "http://kb4images.com/images/image/37185176-image.jpg",
                "http://kb4images.com/images/image/37185176-image.jpg","image",1000,
                "Chicago","IL"));




        return items;
    }

    public void goToMediaActivity(String mediaType, String url) {
        Intent mediaIntent = new Intent(this, MediaActivity.class);
        mediaIntent.putExtra("token", token);
        mediaIntent.putExtra("mediaType", mediaType);
        mediaIntent.putExtra("url", url);
        startActivity(mediaIntent);
    }

    public void goToFeedActivity(String mStateName, String mCityName, String hashtagLabel) {
        Intent feedIntent = new Intent(this, FeedActivity.class);
        feedIntent.putExtra("token", token);
        feedIntent.putExtra("state", mStateName);
        feedIntent.putExtra("city", mCityName);
        feedIntent.putExtra("hashtag", hashtagLabel);
        startActivity(feedIntent);
    }

    public void goToCommentsActivity(int position, String originalUrl, String thumbnailUrl, String mediaType, long mediaId, int likesCount, String hashtag){

        Intent commentIntent = new Intent(this, CommentsActivity.class);
        commentIntent.putExtra("token", token);
        commentIntent.putExtra("hashtag", hashtag);
        commentIntent.putExtra("position", position);
        commentIntent.putExtra("mediaType", mediaType);
        commentIntent.putExtra("originalUrl", originalUrl);
        commentIntent.putExtra("thumbnailUrl", thumbnailUrl);
        commentIntent.putExtra("mediaId", mediaId);
        commentIntent.putExtra("likesCount", likesCount);
        startActivity(commentIntent);
    }

    public void deletePost(final int myPostsItem, long mediaId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        allItems.remove(myPostsItem);
                        myPostsAdapter = new MyPostsAdapter(PostsOfYouActivity.this, allItems);
                        myPostsAdapter.notifyDataSetChanged();
                        gridview.setAdapter(myPostsAdapter);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
