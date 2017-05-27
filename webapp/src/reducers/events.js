import update from 'immutability-helper'

export default(state = [], payload = {}) => {
    switch (payload.type) {
        case 'Event Update':
            console.log('Event: ', payload.event)
            return update(state, {$push: payload.event})
        default:
            return state
    }
}
