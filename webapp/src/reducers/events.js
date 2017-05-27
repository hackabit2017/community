import update from 'immutability-helper'

export default(state = [], payload = {}) => {
    switch (payload.type) {
        case 'Event Update':
            return update(state, {$push: payload.event})
        default:
            return state
    }
}
