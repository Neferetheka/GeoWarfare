package com.aerilys.geowarfare.android.activities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.aerilys.geowarfare.android.R;
import com.aerilys.geowarfare.android.api.foursquare.FoursquareManager;
import com.aerilys.geowarfare.android.api.foursquare.Venue;
import com.aerilys.geowarfare.android.models.Sector;
import com.aerilys.geowarfare.android.tools.Converter;
import com.aerilys.geowarfare.android.tools.DataContainer;
import com.aerilys.geowarfare.android.tools.NetworkHelper;
import com.aerilys.geowarfare.android.tools.SectorOverlay;
import com.aerilys.geowarfare.android.tools.TabManager;
import com.aerilys.geowarfare.android.tools.UIHelper;
import com.aerilys.geowarfare.android.views.adapters.VenueAdapter;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@EActivity(R.layout.activity_checkin)
public class CheckinActivity extends SherlockMapActivity
{
	private SectorOverlay itemizedoverlay;
	private List<Overlay> mapOverlays = new ArrayList<Overlay>();
	private MapController mapController;
	private Location lastKnownLocation = null;
	private LocationManager locationManager;
	public static Sector currentSector;
	boolean isLoading = false;
	List<Venue> listVenues;
	boolean hasToShareOnFacebook = false;

	GeoPoint locationPointToShow = null;
	private MapView mapView;
	private List<Venue> nearByVenues;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	public void bindDatas()
	{
		getSherlock().getActionBar().setDisplayHomeAsUpEnabled(true);

		if (DataContainer.getPlayerI() == null)
			this.finish();

		getSherlock().getActionBar().setTitle(R.string.map);
		getSherlock().setProgressBarIndeterminateVisibility(true);

		try
		{
			mapView = (MapView) findViewById(R.id.mapView);
			mapView.setBuiltInZoomControls(true);
			mapView.displayZoomControls(true);

			mapOverlays = mapView.getOverlays();
			mapController = mapView.getController();
			mapController.setZoom(20);
			mapView.setSatellite(true);

			Drawable drawable = this.getResources().getDrawable(R.drawable.ic_checkin);
			itemizedoverlay = new SectorOverlay(drawable, this);

			locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

			// Define a listener that responds to location updates
			LocationListener locationListener = new LocationListener()
			{
				public void onLocationChanged(Location location)
				{
					if (locationPointToShow == null)
						locationUpdated(location);
				}

				public void onStatusChanged(String provider, int status, Bundle extras)
				{
				}

				public void onProviderEnabled(String provider)
				{
				}

				public void onProviderDisabled(String provider)
				{
				}
			};

			// Register the listener with the Location Manager to receive
			// location
			// updates
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 500, locationListener);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600, 500, locationListener);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			UIHelper.toast(this, getString(R.string.error_map), true);
		}
	}

	@Override
	protected void onResume()
	{
		DataContainer.getInstance().currentSector = null;
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
			this.finish();

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}

	protected void locationUpdated(Location location)
	{
		GeoPoint point;
		lastKnownLocation = location;
		try
		{
			point = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
			isLoading = true;
			changeLoading();
			mapController.animateTo(point);
			mapController.setZoom(20);
		}
		catch (Exception e)
		{
			UIHelper.toast(this, getString(R.string.impossible_determine_location), true);
			e.printStackTrace();
			return;
		}

		try
		{

			isLoading = true;
			changeLoading();

			try
			{
				OverlayItem overlayitem = new OverlayItem(point, "sectors ", "label");

				itemizedoverlay.clearOverlays();
				itemizedoverlay.addOverlay(overlayitem);
				mapOverlays.add(itemizedoverlay);
				mapController.animateTo(overlayitem.getPoint());
				mapController.setZoom(20);
			}
			catch (Exception e)
			{
				UIHelper.toastError(this, e);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			isLoading = false;
			changeLoading();
		}
		isLoading = false;
		changeLoading();
	}

	public void secteurOverlayFocus()
	{
		if (isLoading)
			return;

		mapView.clearFocus();
		mapView.requestFocus();

		isLoading = true;
		changeLoading();

		loadVenues();
	}

	@Background
	protected void loadVenues()
	{
		try
		{
			nearByVenues = FoursquareManager.getVenuesAroundMe(lastKnownLocation.getLatitude(),
					lastKnownLocation.getLongitude());

			loadVenuesCompleted();
		}
		catch (Exception e)
		{
			UIHelper.toastError(this, e);
		}
	}

	@UiThread
	protected void loadVenuesCompleted()
	{
		if (nearByVenues.size() == 0)
		{
			UIHelper.toast(this, getString(R.string.error_foursquare), true);
			return;
		}

		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.dialog_venues);
		dialog.setTitle(R.string.choose_local_sector);

		ListView venuesListview = (ListView) dialog.findViewById(R.id.dialogVenuesListview);
		venuesListview.setAdapter(new VenueAdapter(this, nearByVenues));

		venuesListview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
			{
				isLoading = true;
				changeLoading();

				selectVenue(position);
				dialog.dismiss();
			}
		});

		dialog.show();

		isLoading = false;
		changeLoading();
	}

	@Background
	protected void selectVenue(int position)
	{
		final Venue selectedVenue = nearByVenues.get(position);

		try
		{
			String result = NetworkHelper.HttpRequest(DataContainer.HOST + "checkin?key="
					+ DataContainer.getPlayerI().getKey() + "&lon=" + selectedVenue.getLongitude() + "&lat="
					+ selectedVenue.getLatitude() + "&a=" + lastKnownLocation.getAltitude() + "&venueName="
					+ selectedVenue.Name.replace(" ", "SPACE") + "&venueId=" + selectedVenue.Id + "&cityName="
					+ selectedVenue.City.replace(" ", "SPACE") + "&venueType="
					+ selectedVenue.Category.replace(" ", "SPACE"));

			Gson gson = new Gson();
			Type listType = new TypeToken<Sector>()
			{
			}.getType();
			currentSector = gson.fromJson(result, listType);
			currentSector.setImage(selectedVenue.Image);
			currentSector.setCategory(selectedVenue.Category);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		selectVenueCompleted();
	}

	@UiThread
	protected void selectVenueCompleted()
	{
		isLoading = false;
		changeLoading();

		if (currentSector == null)
		{
			UIHelper.toastConnexion(this);
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(currentSector.getName());
		builder.setCancelable(true);

		if (currentSector.getOwner().length() < 1)
		{
			builder.setMessage(getString(R.string.neutral_sector));
		}
		else
			builder.setMessage(getString(R.string.zone_possessed_by) + currentSector.getOwner() + ".");

		if (!DataContainer.getPlayerI().getLogin().toLowerCase().equals(currentSector.getOwner().toLowerCase()))
		{
			builder.setPositiveButton(R.string.conquest, new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();

					DataContainer.getInstance().currentSector = currentSector;
					TabManager.navigate(CheckinActivity.this, BattleActivity_.class);
				}
			});
		}
		else
		{
			builder.setPositiveButton(getString(R.string.troop_management), new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();

					manageUnitsOpenDialog();
				}
			});
		}
		builder.setNegativeButton(R.string.close, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});

		builder.create().show();

	}

	protected void manageUnitsOpenDialog()
	{
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.dialog_manage_units);
		dialog.setTitle(currentSector.getName());
		dialog.setCanceledOnTouchOutside(true);

		((TextView) dialog.findViewById(R.id.manageUnitsSectorUnits)).setText(getString(R.string.units_in_sector)
				+ currentSector.getUnits());
		((TextView) dialog.findViewById(R.id.manageUnitsReserveUnits)).setText(getString(R.string.units_in_reserve)
				+ DataContainer.getPlayerI().getUnits());

		final EditText manageUnitsEdits = (EditText) dialog.findViewById(R.id.manageUnitsEdit);

		((Button) dialog.findViewById(R.id.manageUnitsButton)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int unitsCount = Converter.ctI(manageUnitsEdits.getText().toString());
				if (unitsCount <= 0)
					UIHelper.toast(CheckinActivity.this, "", true);
				else
				{
					manageUnitsPutArmy(unitsCount);
					dialog.dismiss();
				}
			}
		});

		dialog.show();
	}

	@Background
	protected void manageUnitsPutArmy(int unitsCount)
	{
		isLoading = true;
		changeLoading();

		String result = null;
		try
		{
			String url = DataContainer.HOST + "putArmy?key=" + DataContainer.getPlayerI().getKey() + "&venueId="
					+ currentSector.getVenueId() + "&units=" + unitsCount;
			result = NetworkHelper.HttpRequest(url);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		manageUnitsPutArmyCompleted(result, unitsCount);
	}

	@UiThread
	protected void manageUnitsPutArmyCompleted(String result, int unitsCount)
	{
		isLoading = false;
		changeLoading();
		
		if(result == null)
		{
			UIHelper.toastConnexion(this);
		}
		else
		{
			UIHelper.toast(this, getString(R.string.deployment_finished), true);
			currentSector.setUnits(currentSector.getUnits()+unitsCount);
			
			DataContainer.getPlayerI().getSectorByVenueId(currentSector.getVenueId()).setUnits(currentSector.getUnits());
		}
	}

	@UiThread
	protected void changeLoading()
	{
		if (isLoading)
			setSupportProgressBarIndeterminateVisibility(true);
		else
			setSupportProgressBarIndeterminateVisibility(false);

	}
}
