package com.yolomate.Zentube.info_list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.yolomate.Zentube.R;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

public class InfoItemDialog {
    private final AlertDialog dialog;

    public InfoItemDialog(@NonNull final Activity activity,
                          @NonNull final StreamInfoItem info,
                          @NonNull final String[] commands,
                          @NonNull final DialogInterface.OnClickListener actions) {
        this(activity, commands, actions, info.getName(), info.getUploaderName());
    }

    public InfoItemDialog(@NonNull final Activity activity,
                          @NonNull final String[] commands,
                          @NonNull final DialogInterface.OnClickListener actions,
                          @NonNull final String title,
                          @Nullable final String additionalDetail) {

        final View bannerView = View.inflate(activity, R.layout.dialog_title, null);
        bannerView.setSelected(true);

        TextView titleView = bannerView.findViewById(R.id.itemTitleView);
        titleView.setText(title);

        TextView detailsView = bannerView.findViewById(R.id.itemAdditionalDetails);
        if (additionalDetail != null) {
            detailsView.setText(additionalDetail);
            detailsView.setVisibility(View.VISIBLE);
        } else {
            detailsView.setVisibility(View.GONE);
        }

        dialog = new AlertDialog.Builder(activity)
                .setCustomTitle(bannerView)
                .setItems(commands, actions)
                .create();
    }

    public void show() {
        dialog.show();
    }
}
