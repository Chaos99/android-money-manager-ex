package com.money.manager.ex.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import com.money.manager.ex.MoneyManagerApplication;
import com.money.manager.ex.R;
import com.money.manager.ex.database.MoneyManagerOpenHelper;
import com.money.manager.ex.database.QueryCategorySubCategory;
import com.money.manager.ex.database.TableCategory;
import com.money.manager.ex.database.TableInfoTable;
import com.money.manager.ex.database.TableSubCategory;
import com.money.manager.ex.dropbox.SimpleCrypto;

public class Core {
	private static final String LOGCAT = Core.class.getSimpleName();
	
	public static final int INVALID_ATTRIBUTE = -1;
	public static final String INFO_NAME_USERNAME = "USERNAME";
	public static final String INFO_NAME_DATEFORMAT = "DATEFORMAT";
	public static final String INFO_NAME_FINANCIAL_YEAR_START_DAY = "FINANCIAL_YEAR_START_DAY";
	public static final String INFO_NAME_FINANCIAL_YEAR_START_MONTH = "FINANCIAL_YEAR_START_MONTH";
	public static final String INFO_SKU_ORDER_ID = "SKU_ORDER_ID";
	//Base64 and Token
	private static final String base64 = "A346EA3CFF9C3F679946C4294AAF737D1E2708694B66A13D1A615F7C0752F4770664F8407DDCE84AB1DE2770DA62760D2ACF6F45F9DCDDB7E5018D9F5FCD561544C6F4BDFF7801BC1DF65A97C40AC293A3C097C4E3F3CDD5366EAB72B29CA78D234B69C30047201323D7070BCC42EA02A9903955783CC701FB3B3CCB9855B16D70EAC0931668FD33E1C7634EAEECAC93D1980D3B81DB10A50905815A54FFEAE951DDA6862561B971D1F439FE49A146F33FF661E79533F574F187377DE7229433ABBFF7ACEC1C05BC38520BB537D1418239077A696AA00E415980922AD575B39E83B70B04C836D2E9BD9398884469A27E5826DAF1FF77D6DE687BE17ACA2243F25901168FD37E14717D38737A963175E4131B819B475A700A3532F10CA5A3028ACC4061217B7CD0EF070FB9B0354BD8D0F95719E5C1F27BB227CA62EBE1B6C04BFB31A5592701F263023A14A2D27231D44B60A5D0B5394154C786C87D0911C566640C1F67A223A302BF19E47137CEBF2DDFC3594E1AE0962A135BBEF9355AADB28AD494AEA033CB9C877254676844D0F246AAE95F7D95932929A01675E8AEC76097E46702514A60FF6A3A7C72C848D002CD1EEEA424FACE609126134C2E24A8D93B2777F3DE24CE8C557ABA1B293DEC5119A62DAE6249EAD0EB88A9B4E1D8729968A8098AF61A2BD67446F692F665F891F1909B1CA66AA6872524E7F7C6EB7EF79D87D6FD6665149F5FC94362533D9AB7CB51BCB6B4599BA217F82220C5217F6AC6D138A3B8E5D8DAC4DE6618B435F006E8426065593347411CD6BE9CECA39A23D693C7A072F75937F5BE454119D780E9623E1424F14631EF693ECAD21438473E5813DF6D7B5984693441AD8C15EA4543BC958AEE8F8C0AF587D18893AA3584268091B606A04B0B099B5AD3D97D5347BFB09F64B3CA7F3836D11EDF2C56C436877658A54C86ABEDE5BB86E0C3324E52CCEDD1DD1F41D347ECEE8E8566FCF481EA4FD5841F3102AB25228AD93A7A89C9728E18A1315EA46CCD94BA84A62EDFB65A992DDC8BC87983AE9F11AA606DCE191E209073B94B40760820C024EBD717D2D4D66F459EF7A7C577371CD88864B8E94E48D4883D623EB1EB1CDB8109157C6B2444F79869F2FE56C0A4CCA491DBE6A69F";
	@SuppressWarnings("unused")
	private static final String token = "1E1380731503B92242AB2A5D8A41F8AE761A9ECD8BC7BBC9C229C5B49F8A032C59467A21872756BF85B7CB781DDB1DE6C1D1D77C9D5E7EBCA07CD01E2525CE4B186128463FAC8C99956BF102DC28A56647F15248624DF792B7D437699024102D24B48B5FAFDE18E690F29224A6C233493D60696A1CF018F002C9CC4A057548665829B552B2F42F2CAACC5F47F5EC6A01B4F525AC2E0E07AEF2D18B871E1145066D5AF20F32D69B0CEDE9D0B0CEC313E34DF0B55F2A7D66A7CEBF481F877BDF04C9E7CEF1BC1F230342EF07C952D8C9A584B2D4D572D470075F2F2306D3F07785340BB49D6FED77E7A1F0CC30F79F21C774B0C298637E23C2A9FD4ECAB9E74A53B074A25B6566D3995DF2D25998A818B439273761E9A911EF73A118C3FE22CCBA4E5FA461501EE9E0E5A6E99942758BC9D9A9D12672A70E388437F2374C48314D13E9205973B7A452EA9FE97CFFCF3A966F02931EFBB5CE3AB70D5AEF2E9670A8B1C3CFF91889D4CAA4A176495954F0F95D4B71562527AB9E596D1F8A7147E221FD6E6C434740CED20D9422AC6ED96A48";
	
	
	public static String getAppBase64() {
		try {
			return SimpleCrypto.decrypt(MoneyManagerApplication.KEY, getTokenApp()); 
		} catch (Exception e) {
			Log.e(LOGCAT, e.getMessage());
		}
		return null;
	}

	private static String getTokenApp() {
		try {
			return SimpleCrypto.decrypt(MoneyManagerApplication.DROPBOX_APP_KEY, base64); 
		} catch (Exception e) {
			Log.e(LOGCAT, e.getMessage());
		}
		return null;
	}

	private Context context;
	
	public Core(Context context) {
		super();
		this.context = context;
	}
	/**
	 * Shown alert dialog
	 * @param resId id of string 
	 * @return
	 */
	public static AlertDialog alertDialog(Context ctx, int resId) {
		return alertDialog(ctx, ctx.getString(resId));
	}
	/**
	 * Shown alert dialog
	 * @param text to display
	 * @return
	 */
	public static AlertDialog alertDialog(Context ctx, String text) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
		// setting alert dialog
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setTitle(R.string.attention);
		dialog.setMessage(text);
		dialog.setNeutralButton(android.R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		// show dialog
		return dialog.create();
	}
	
	public File backupDatabase() {
		File database = new File(MoneyManagerApplication.getDatabasePath(context));
		if (database == null || !database.exists())
			return null;
		//get external storage
		File externalStorage = Environment.getExternalStorageDirectory();
		if (!(externalStorage != null && externalStorage.exists() && externalStorage.isDirectory()))
			return null;
		//create folder to copy database
		File folderOutput = new File(externalStorage + "/" + context.getPackageName());
		//make a directory
		if (!folderOutput.exists()) {
			if (!folderOutput.mkdirs())
				return null;
		}
		//take a folder of database
		ArrayList<File> filesFromCopy = new ArrayList<File>();
		//add current database
		filesFromCopy.add(database);
		//get file journal
		File folder = database.getParentFile();
		if (folder != null) {
			for(File file : folder.listFiles()) {
				if (file.getName().startsWith(database.getName()) && !database.getName().equals(file.getName())) {
					filesFromCopy.add(file);
				}
			}
		}
		//copy all files
		for (int i = 0; i < filesFromCopy.size(); i ++) {
			try {
				copy(filesFromCopy.get(i), new File(folderOutput + "/" + filesFromCopy.get(i).getName()));
			} catch (Exception e) {
				Log.e(LOGCAT, e.getMessage());
				return null;
			}
		}
		
		return new File(folderOutput + "/" + filesFromCopy.get(0).getName());
	}
	
	public void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}
	
	/**
	 * Returns category and sub-category formatted
	 * @param categoryId
	 * @param subCategoryId
	 * @return category : sub-category
	 */
	public String getCategSubName(int categoryId, int subCategoryId) {
		String categoryName, subCategoryName, ret;
		TableCategory category = new TableCategory();
		TableSubCategory subCategory  = new TableSubCategory();
		Cursor cursor;
		MoneyManagerOpenHelper helper = new MoneyManagerOpenHelper(context);
		// category
		cursor = helper.getReadableDatabase().query(category.getSource(), null, TableCategory.CATEGID + "=?", new String[] {Integer.toString(categoryId)}, null, null, null);
		if ((cursor != null) && (cursor.moveToFirst())) {
			// set category name and sub category name
			categoryName = cursor.getString(cursor.getColumnIndex(TableCategory.CATEGNAME));
		} else {
			categoryName =  null;
		}
		cursor.close();
		// sub-category
		cursor = helper.getReadableDatabase().query(subCategory.getSource(), null, TableSubCategory.SUBCATEGID + "=?", new String[] {Integer.toString(subCategoryId)}, null, null, null);
		if ((cursor != null) && (cursor.moveToFirst())) {
			// set category name and sub category name
			subCategoryName = cursor.getString(cursor.getColumnIndex(TableSubCategory.SUBCATEGNAME));
		} else {
			subCategoryName =  null;
		}
		cursor.close();
		helper.close();
		
		ret = (!TextUtils.isEmpty(categoryName) ? categoryName : "") + (!TextUtils.isEmpty(subCategoryName) ? ":" + subCategoryName : "");
		
		return ret;
	}
	
	/**
	 * Returns category and sub-category formatted
	 * @param QueryCategorySubCategory object
	 * @return category : sub-category
	 */
	public String getCategSubName(QueryCategorySubCategory queryCategorySubCategory) {
		return getCategSubName(queryCategorySubCategory.getCategId(), queryCategorySubCategory.getSubCategId());
	}
	
	/**
	 * Returns category and sub-category formatted
	 * @param category object
	 * @return category : sub-category
	 */
	public String getCategSubName(TableCategory category) {
		return getCategSubName(category.getCategId(), -1);
	}
	
	/**
	 * Returns category and sub-category formatted
	 * @param subcategory object
	 * @return category : sub-category
	 */
	public String getCategSubName(TableSubCategory subCategory) {
		return getCategSubName(subCategory.getCategId(), subCategory.getSubCategId());
	}
	
	/**
	 * Retrieve value of info
	 * @param info to be retrieve
	 * @return value
	 */
	public String getInfoValue(String info) {
		TableInfoTable infoTable = new TableInfoTable();
		MoneyManagerOpenHelper helper = null;
		Cursor data = null;
		String ret = null;

		try {
			helper = new MoneyManagerOpenHelper(context);
			data = helper.getReadableDatabase().query(infoTable.getSource(), null, TableInfoTable.INFONAME + "=?", new String[] {info}, null, null, null);
			if (data != null && data.moveToFirst()) {
				ret = data.getString(data.getColumnIndex(TableInfoTable.INFOVALUE));
			}
		} catch (Exception e) {
			Log.e(LOGCAT, e.getMessage());
		} finally {
			// close data
			if (data != null)
				data.close();
			if (helper != null)
				helper.close();
		}
		
		return ret;
	}
	
	/**
	 * Return arrays of month formatted and localizated
	 * @return arrays of months
	 */
	public String[] getListMonths() {
		return new DateFormatSymbols().getMonths();
	}
	
	/**
	 * Resolves the id attribute in color
	 * 
	 * @param attr id attribute
	 * @return color
	 */
	public int resolveColorAttribute(int attr) {
		return context.getResources().getColor(resolveIdAttribute(attr));
	}
	/**
	 * Resolve the id attribute into int value
	 * @param attr id attribute
	 * @return
	 */
	public int resolveIdAttribute(int attr) {
		TypedValue tv = new TypedValue();
		if (context.getTheme().resolveAttribute(attr, tv, true))
			return tv.resourceId;
		else 
			return INVALID_ATTRIBUTE;
	}
	
	/**
	 * Update value of info
	 * @param info to be updated
	 * @param value
	 * @return true if update success otherwise false
	 */
	public boolean setInfoValue(String info, String value) {
		boolean ret = true;
		TableInfoTable infoTable = new TableInfoTable();
		MoneyManagerOpenHelper helper = null;
		boolean exists = false;
		// check if exists info
		exists = !TextUtils.isEmpty(getInfoValue(info));
		// content values
		ContentValues values = new ContentValues();
		values.put(TableInfoTable.INFOVALUE, value);
		
		try {
			helper = new MoneyManagerOpenHelper(context);
			if (exists) {
				ret = helper.getWritableDatabase().update(infoTable.getSource(), values, TableInfoTable.INFONAME + "=?", new String[] {info}) >= 0;
			} else {
				values.put(TableInfoTable.INFONAME, info);
				ret = helper.getWritableDatabase().insert(infoTable.getSource(), null, values) >= 0;
			}
		} catch (Exception e) {
			Log.e(LOGCAT, e.getMessage());
			ret = false;
		} finally {
			if (helper != null) helper.close();
		}
		
		return ret;
	}
}