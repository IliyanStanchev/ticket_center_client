package com.example.ticket_center_client.views.organizer.distributors;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticket_center_client.R;
import com.example.ticket_center_client.adapters.RatingAdapter;
import com.example.ticket_center_client.requests.APICallListener;
import com.example.ticket_center_client.requests.ResponseDeserializer;
import com.example.ticket_center_client.services.RatingService;

import java.time.LocalDateTime;

import tuvarna.ticket_center_common.models.DistributorModel;
import tuvarna.ticket_center_common.models.RatingModel;
import tuvarna.ticket_center_common.models.RatingsWrapper;
import tuvarna.ticket_center_common.models.UserModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;

public class DistributorFragment extends Fragment implements APICallListener {

    RatingAdapter ratingAdapter = new RatingAdapter();

    private AlertDialog.Builder builder;

    private AlertDialog dialog;

    private DistributorModel distributor;

    private UserModel user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distributor, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        distributor = (DistributorModel) getArguments().getSerializable("selectedDistributor");
        if (distributor == null) {
            return view;
        }

        user = (UserModel) getActivity().getIntent().getSerializableExtra("userModel");
        if (user == null) {
            return view;
        }

        TextView tvName = view.findViewById(R.id.tvName);
        tvName.setTextSize(22);

        TextView tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvPhoneNumber.setTextSize(22);

        TextView tvHonorarium = view.findViewById(R.id.tvHonorarium);
        tvHonorarium.setTextSize(22);

        tvName.setText("Name: " + distributor.getFirstName() + " " + distributor.getLastName());
        tvPhoneNumber.setText("Phone: " + distributor.getPhoneNumber());
        tvHonorarium.setText("Honorarium: " + distributor.getHonorarium() + " BGN");

        RecyclerView recyclerView = view.findViewById(R.id.rvRates);

        recyclerView.setAdapter(ratingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ratingAdapter.setRatings(distributor.getRatings());
        ratingAdapter.notifyDataSetChanged();

        Button btnGoBack = view.findViewById(R.id.btnBack);

        btnGoBack.setOnClickListener(v -> {

            DistributorsFragment distributorsFragment = new DistributorsFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_distributor_id, distributorsFragment, "distributorsFragment")
                    .addToBackStack(null)
                    .commit();
        });

        Button btnRate = view.findViewById(R.id.btnAdd);

        btnRate.setOnClickListener(v -> review());

        return view;
    }

    private void review() {

        builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_review, null);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        ratingBar.setIsIndicator(false);
        EditText etComment = view.findViewById(R.id.etComment);

        Button btnAction = view.findViewById(R.id.btnAction);
        Button btnGoBack = view.findViewById(R.id.btnGoBack);

        btnAction.setText("Rate");

        btnAction.setOnClickListener(v -> {
            RatingService ratingService = new RatingService(DistributorFragment.this);
            ratingService.addRating(new RatingModel((int) ratingBar.getRating(), etComment.getText().toString(), distributor.getEmail(), user.getEmail(), null, LocalDateTime.now()));
        });

        btnGoBack.setOnClickListener(v -> dialog.dismiss());

        builder.setView(view);
        dialog = builder.create();

        dialog.show();
    }

    @Override
    public void onAPICallSuccess(CommonResponse response) {

        if (response.getRequestCode() == RequestCodes.RATINGS_POST) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {

                RatingService ratingService = new RatingService(DistributorFragment.this);
                ratingService.getRatings(distributor.getEmail());
            }
        }

        if (response.getRequestCode() == RequestCodes.RATINGS_GET) {
            if (response.getRequestStatus() == RequestStatuses.SUCCESS) {

                RatingsWrapper wrapper = ResponseDeserializer.deserialize(response.getResponse(), RatingsWrapper.class);
                distributor.setRatings(wrapper.getRatings());

                ratingAdapter.setRatings(distributor.getRatings());
                ratingAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onAPICallFailure() {

    }
}
