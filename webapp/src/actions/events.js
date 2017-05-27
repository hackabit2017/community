import database from '../database'

export const eventupdate = (event) => {
    return {
        type: 'Event Update',
        event
    };
}

export function getEvents() {
    return dispatch => {
        return database.ref('/events').once('value', snap => {
            const event = snap.val();
            dispatch(eventupdate(event))
        })
            .catch((error) => {
                console.log(error);
            });
    }
}