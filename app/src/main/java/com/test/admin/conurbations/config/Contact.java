package com.test.admin.conurbations.config;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.utils.PinyinUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Created by admin on 2016/9/18.
 */

public class Contact {
    private String index;
    private String name;
    private String number;
    private Bitmap contactPhoto;

    /**
     * 由于手机通讯录中的手机联系人可能从手机本地和SIM卡中获取
     * 所以需要去重，本类利用hashSet对list去重，需要重写hashCode
     * 方法
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contact contact = (Contact) obj;
        if (!name.equals(contact.name)) return false;
        return number.equals(contact.number);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + number.hashCode();
        return result;
    }

    public Contact(String index, String name, String number, Bitmap contactPhoto) {
        this.index = index;
        this.name = name;
        this.number = number;
        this.contactPhoto = contactPhoto;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Bitmap getContactPhoto() {
        return contactPhoto;
    }

    public static ArrayList<Contact> phoneInfoList = new ArrayList<>();

    public static ArrayList<Contact> getNumber(Context context) {

        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        ContentResolver resolver = context.getContentResolver();
        String phoneNumber;
        String phoneName;
        String phoneIndex;

        Long contactId;
        Long photoId;
        Bitmap contactPhoto = null;
        while (cursor.moveToNext()) {
            //获取通讯录电话号码
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //获取通讯录联系人姓名
            phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //根据联系人姓名获得其名字的首字母（并转成大写）
            phoneIndex = PinyinUtil.hanziToPinyin(phoneName).substring(0, 1).toUpperCase();
            //获取通讯录联系人ID
            contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            //获取通讯录联系人头像ID
            photoId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
            //获取手机联系人头像
            if (photoId > 0) {
                Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
                InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                contactPhoto = BitmapFactory.decodeStream(input);
            } else {
                //没有头像或者获取失败给默认头像
                contactPhoto = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            }
            Contact info = new Contact(phoneIndex, phoneName, phoneNumber, contactPhoto);
            //Log.i("number:"+phoneIndex,phoneName+":"+phoneNumber);
            phoneInfoList.add(info);
        }

        //利用HashSet对List去重
        phoneInfoList = new ArrayList<>(new HashSet<>(phoneInfoList));
        //按照字母从A-Z排序
        Collections.sort(phoneInfoList, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.index.compareTo(o2.index);
            }
        });
        return phoneInfoList;
    }
}
