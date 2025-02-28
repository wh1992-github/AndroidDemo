//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.group.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public final class LocalBroadcastManager {
    private static final String TAG = "LocalBroadcastManager";
    private static final boolean DEBUG = false;
    private final Context mAppContext;
    private final HashMap<CustomBroadcastReceiver, ArrayList<LocalBroadcastManager.ReceiverRecord>> mReceivers = new HashMap<>();
    private final HashMap<String, ArrayList<LocalBroadcastManager.ReceiverRecord>> mActions = new HashMap<>();
    private final ArrayList<LocalBroadcastManager.BroadcastRecord> mPendingBroadcasts = new ArrayList<>();
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private final Handler mHandler;
    private static final Object mLock = new Object();
    private static LocalBroadcastManager mInstance;

    @NonNull
    public static LocalBroadcastManager getInstance(@NonNull Context context) {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new LocalBroadcastManager(context.getApplicationContext());
            }

            return mInstance;
        }
    }

    private LocalBroadcastManager(Context context) {
        this.mAppContext = context;
        this.mHandler = new Handler(context.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        LocalBroadcastManager.this.executePendingBroadcasts();
                        break;
                    default:
                        super.handleMessage(msg);
                }

            }
        };
    }

    public void registerReceiver(@NonNull CustomBroadcastReceiver receiver, @NonNull IntentFilter filter) {
        synchronized (this.mReceivers) {
            LocalBroadcastManager.ReceiverRecord entry = new LocalBroadcastManager.ReceiverRecord(filter, receiver);
            ArrayList<LocalBroadcastManager.ReceiverRecord> filters = (ArrayList) this.mReceivers.get(receiver);
            if (filters == null) {
                filters = new ArrayList(1);
                this.mReceivers.put(receiver, filters);
            }

            filters.add(entry);

            for (int i = 0; i < filter.countActions(); ++i) {
                String action = filter.getAction(i);
                ArrayList<LocalBroadcastManager.ReceiverRecord> entries = (ArrayList) this.mActions.get(action);
                if (entries == null) {
                    entries = new ArrayList(1);
                    this.mActions.put(action, entries);
                }

                entries.add(entry);
            }

        }
    }

    public void unregisterReceiver(@NonNull CustomBroadcastReceiver receiver) {
        synchronized (this.mReceivers) {
            ArrayList<LocalBroadcastManager.ReceiverRecord> filters = (ArrayList) this.mReceivers.remove(receiver);
            if (filters != null) {
                for (int i = filters.size() - 1; i >= 0; --i) {
                    LocalBroadcastManager.ReceiverRecord filter = (LocalBroadcastManager.ReceiverRecord) filters.get(i);
                    filter.dead = true;

                    for (int j = 0; j < filter.filter.countActions(); ++j) {
                        String action = filter.filter.getAction(j);
                        ArrayList<LocalBroadcastManager.ReceiverRecord> receivers = (ArrayList) this.mActions.get(action);
                        if (receivers != null) {
                            for (int k = receivers.size() - 1; k >= 0; --k) {
                                LocalBroadcastManager.ReceiverRecord rec = (LocalBroadcastManager.ReceiverRecord) receivers.get(k);
                                if (rec.receiver == receiver) {
                                    rec.dead = true;
                                    receivers.remove(k);
                                }
                            }

                            if (receivers.size() <= 0) {
                                this.mActions.remove(action);
                            }
                        }
                    }
                }

            }
        }
    }

    public boolean sendBroadcast(@NonNull Intent intent) {
        synchronized (this.mReceivers) {
            String action = intent.getAction();
            String type = intent.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
            Uri data = intent.getData();
            String scheme = intent.getScheme();
            Set<String> categories = intent.getCategories();
            boolean debug = (intent.getFlags() & 8) != 0;
            if (debug) {
                Log.v("LocalBroadcastManager", "Resolving type " + type + " scheme " + scheme + " of intent " + intent);
            }

            ArrayList<LocalBroadcastManager.ReceiverRecord> entries = (ArrayList) this.mActions.get(intent.getAction());
            if (entries != null) {
                if (debug) {
                    Log.v("LocalBroadcastManager", "Action list: " + entries);
                }

                ArrayList<LocalBroadcastManager.ReceiverRecord> receivers = null;

                int i;
                for (i = 0; i < entries.size(); ++i) {
                    LocalBroadcastManager.ReceiverRecord receiver = (LocalBroadcastManager.ReceiverRecord) entries.get(i);
                    if (debug) {
                        Log.v("LocalBroadcastManager", "Matching against filter " + receiver.filter);
                    }

                    if (receiver.broadcasting) {
                        if (debug) {
                            Log.v("LocalBroadcastManager", "  Filter's target already added");
                        }
                    } else {
                        int match = receiver.filter.match(action, type, scheme, data, categories, "LocalBroadcastManager");
                        if (match >= 0) {
                            if (debug) {
                                Log.v("LocalBroadcastManager", "  Filter matched!  match=0x" + Integer.toHexString(match));
                            }

                            if (receivers == null) {
                                receivers = new ArrayList();
                            }

                            receivers.add(receiver);
                            receiver.broadcasting = true;
                        } else if (debug) {
                            String reason;
                            switch (match) {
                                case -4:
                                    reason = "category";
                                    break;
                                case -3:
                                    reason = "action";
                                    break;
                                case -2:
                                    reason = "data";
                                    break;
                                case -1:
                                    reason = "type";
                                    break;
                                default:
                                    reason = "unknown reason";
                            }

                            Log.v("LocalBroadcastManager", "  Filter did not match: " + reason);
                        }
                    }
                }

                if (receivers != null) {
                    for (i = 0; i < receivers.size(); ++i) {
                        ((LocalBroadcastManager.ReceiverRecord) receivers.get(i)).broadcasting = false;
                    }

                    this.mPendingBroadcasts.add(new LocalBroadcastManager.BroadcastRecord(intent, receivers));
                    if (!this.mHandler.hasMessages(1)) {
                        this.mHandler.sendEmptyMessage(1);
                    }

                    return true;
                }
            }

            return false;
        }
    }

    public void sendBroadcastSync(@NonNull Intent intent) {
        if (this.sendBroadcast(intent)) {
            this.executePendingBroadcasts();
        }

    }

    void executePendingBroadcasts() {
        while (true) {
            LocalBroadcastManager.BroadcastRecord[] brs;
            synchronized (this.mReceivers) {
                int N = this.mPendingBroadcasts.size();
                if (N <= 0) {
                    return;
                }

                brs = new LocalBroadcastManager.BroadcastRecord[N];
                this.mPendingBroadcasts.toArray(brs);
                this.mPendingBroadcasts.clear();
            }

            for (int i = 0; i < brs.length; ++i) {
                LocalBroadcastManager.BroadcastRecord br = brs[i];
                int nbr = br.receivers.size();

                for (int j = 0; j < nbr; ++j) {
                    LocalBroadcastManager.ReceiverRecord rec = (LocalBroadcastManager.ReceiverRecord) br.receivers.get(j);
                    if (!rec.dead) {
                        rec.receiver.onReceive(this.mAppContext, br.intent);
                    }
                }
            }
        }
    }

    private static final class BroadcastRecord {
        final Intent intent;
        final ArrayList<LocalBroadcastManager.ReceiverRecord> receivers;

        BroadcastRecord(Intent _intent, ArrayList<LocalBroadcastManager.ReceiverRecord> _receivers) {
            this.intent = _intent;
            this.receivers = _receivers;
        }
    }

    private static final class ReceiverRecord {
        final IntentFilter filter;
        final CustomBroadcastReceiver receiver;
        boolean broadcasting;
        boolean dead;

        ReceiverRecord(IntentFilter _filter, CustomBroadcastReceiver _receiver) {
            this.filter = _filter;
            this.receiver = _receiver;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder(128);
            builder.append("Receiver{");
            builder.append(this.receiver);
            builder.append(" filter=");
            builder.append(this.filter);
            if (this.dead) {
                builder.append(" DEAD");
            }

            builder.append("}");
            return builder.toString();
        }
    }

    public interface CustomBroadcastReceiver {
        void onReceive(Context context, Intent intent);
    }
}
