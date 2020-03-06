package com.bigBlue.people.ui.adapter;

/**
 * The interface Adapter click handler.
 */
public interface AdapterClickHandler
{
    /**
     * On items clicked.
     *
     * @param position the position
     */
    void onItemsClicked(int position);

    /**
     * On header clicked.
     *
     * @param position the position
     */
    void onHeaderClicked(int position);
}
