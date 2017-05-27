import update from 'immutability-helper'

export default(state = [], payload = {}) => {
    switch (payload.type) {
        case 'GET_EVENTS_FULFILLED':
            return update(state, {$set: payload.events})
        case 'GET_EVENTS_LOCATIONS_FULFILLED':
            const locations = payload.locations
            let newState = Object.assign({}, state)
            Object.keys(locations).forEach(id => {
                const location = locations[id]
                newState[id].lat = location.l[0]
                newState[id].lon = location.l[1]
            })
            return newState
        default:
            return state
    }
}
