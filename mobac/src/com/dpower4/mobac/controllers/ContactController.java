package com.dpower4.mobac.controllers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

import android.provider.CallLog;
import android.provider.ContactsContract;

import com.dpower4.mobac.database.DatabaseHandler;

import com.dpower4.mobac.models.Contact;

import com.dpower4.mobac.services.MobacService;

import com.dpower4.mobac.utils.MobacSdk;

public class ContactController {
	private DatabaseHandler db;
	private Context context;

	public ContactController(Context context) {
		this.context = context;
		this.db = new DatabaseHandler(context);
	}

	public void saveContacts() throws JSONException, Exception {

		int lastId = MobacService.user.getContactId();
		// StringBuffer sb = new StringBuffer();

		Cursor cur = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null,

				CallLog.Calls._ID + " > " + lastId, null, null);

		String phone = null;
		String emailContact = null;
		String emailType = null;

		int i = 0;
		JSONObject bulkContact = new JSONObject();
		JSONObject root = new JSONObject();
		JSONArray contactsArray = new JSONArray();

		MobacSdk mobac = new MobacSdk();

		int currentId = lastId;
		int toSetId = lastId;
		
		// String image_uri = "";

		// Bitmap bitmap = null;

		if (cur.getCount() > 0) {

			while (cur.moveToNext()) {
				i++;

				currentId = cur.getInt(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				if (currentId > toSetId) toSetId = currentId;
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				// image_uri = cur
				// .getString(cur
				// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

					Cursor pCur = context.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { "" + currentId },
							null);

					while (pCur.moveToNext()) {
						phone = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					pCur.close();

					Cursor emailCur = context.getContentResolver().query(
							ContactsContract.CommonDataKinds.Email.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Email.CONTACT_ID
									+ " = ?", new String[] { "" + currentId },
							null);

					while (emailCur.moveToNext()) {
						emailContact = emailCur
								.getString(emailCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						emailType = emailCur
								.getString(emailCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

					}

					emailCur.close();
				}
				Contact contact = new Contact(phone, emailContact, emailType,
						name);
				emailType = "";
				emailContact = "";
				contactsArray.put(contact.getJSONObject());

				if (i >= 20) {

					bulkContact.put("contacts", contactsArray);
					root.put("root", bulkContact);

					mobac.postContacts(root.toString());

					bulkContact.remove("contacts");
					root.remove("root");

					MobacService.user.setContactId(toSetId);
					MobacService.user.setContactCount(MobacService.user.getContactCount() + i);
					
					i = 0;
					contactsArray = new JSONArray();

				}

				/*
				 * if (image_uri != null) { try { bitmap =
				 * MediaStore.Images.Media
				 * .getBitmap(context.getContentResolver(),
				 * Uri.parse(image_uri));
				 * 
				 * } catch (FileNotFoundException e) { // TODO Auto-generated
				 * catch block e.printStackTrace(); } catch (IOException e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); }
				 * 
				 * }
				 */

			}

		}

		cur.close();

		if (i > 0) {

			bulkContact.put("contacts", contactsArray);
			root.put("root", bulkContact);
			MobacService.user.setContactCount(MobacService.user.getContactCount() + i);
			mobac.postContacts(root.toString());

		}
		MobacService.user.setContactId(toSetId);

	}

}
