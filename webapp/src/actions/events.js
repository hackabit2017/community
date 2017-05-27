import database from '../database'

export const getEventsFulfilled = events => {
    return {
        type: 'GET_EVENTS_FULFILLED',
        events
    };
}

export const getEventsLocationsFulfilled = locations => {
    return {
        type: 'GET_EVENTS_LOCATIONS_FULFILLED',
        locations
    }
}

export function getEvents() {
    return dispatch => {
        return database.ref('/events').on('value', snap => {
            const events = snap.val();
            dispatch(getEventsFulfilled(events))
        })
    }
}

export function getLocations() {
    return dispatch => {
        return database.ref('/events_locations').on('value', snap => {
            const locations = snap.val();
            dispatch(getEventsLocationsFulfilled(locations))
        })
    }
}