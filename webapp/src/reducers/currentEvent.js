import update from 'immutability-helper'

export default(state = [], payload = {}) => {
  switch (payload.type) {
    case 'EVENT_WAS_SELECTED':
      return update(state, {$set: payload.event})
    default:
      return state
  }
}
