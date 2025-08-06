package com.aravinda.loginwithgooglesample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.aravinda.loginwithgooglesample.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private static final String TAG = "GoogleSignIn";
    private static final int RC_SIGN_IN = 9001;
    private FragmentFirstBinding binding;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configure Google Sign-In options - using your web client ID
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        Log.d(TAG, "GoogleSignInOptions configured");
        Log.d(TAG, "Requesting email, profile, and ID token");

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        Log.d(TAG, "GoogleSignInClient created");

        binding.signInButton.setOnClickListener(v -> {
            Log.d(TAG, "Sign-in button clicked");
            signIn();
        });
    }

    private void signIn() {
        Log.d(TAG, "Starting sign-in process");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Log.d(TAG, "Sign-in intent created, launching activity");
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult called - requestCode: " + requestCode + ", resultCode: " + resultCode);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.d(TAG, "Processing sign-in result");
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "=== SIGN-IN SUCCESSFUL ===");
            Log.d(TAG, "Account ID: " + account.getId());
            Log.d(TAG, "Account Email: " + account.getEmail());
            Log.d(TAG, "Account Display Name: " + account.getDisplayName());
            Log.d(TAG, "Account Given Name: " + account.getGivenName());
            Log.d(TAG, "Account Family Name: " + account.getFamilyName());
            Log.d(TAG, "Photo URL: " + (account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "null"));
            Log.d(TAG, "=== AUTHORIZATION TOKEN ===");
            Log.d(TAG, "ID Token (Authorization Token): " + account.getIdToken());
            Log.d(TAG, "========================");
            
            // Show success message
            Toast.makeText(getContext(), "Sign-in successful! Check logs for authorization token.", Toast.LENGTH_LONG).show();
            
            // Update UI to show user info
            binding.textviewFirst.setText("Welcome " + account.getDisplayName() + "!\nEmail: " + account.getEmail() + "\n\n✅ Authorization token logged to console");
            
        } catch (ApiException e) {
            Log.w(TAG, "=== SIGN-IN FAILED ===");
            Log.w(TAG, "Error code: " + e.getStatusCode());
            Log.w(TAG, "Error message: " + e.getMessage());
            Log.w(TAG, "==================");
            
            String errorMessage = "Sign-in failed";
            switch (e.getStatusCode()) {
                case 12501: // Cancelled
                    errorMessage = "Sign-in cancelled";
                    break;
                case 7: // Network error
                    errorMessage = "Network error - check internet connection";
                    break;
                case 10: // Developer error
                    errorMessage = "Configuration error - check web client ID";
                    break;
            }
            
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}