package com.tapwisdom.core.daos.documents;

/**
 * Created by srividyak on 03/05/15.
 */
public enum  CallStatus {
    // active => set when turbobridge fires an event indicating conf started
    // waiting => set by us when a call is initiated
    // done => set when turbobridge fired an event indicating conf completed
    // cancelled => set by us when cancel call API is called ??
    // noshow => set by us when startTime and duration is past currentTime using a cronjob which runs on a daily basis.
    active, waiting, done, cancelled, noshow
}
