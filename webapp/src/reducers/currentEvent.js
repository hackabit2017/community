import update from 'immutability-helper'

export default(state = [], payload = {}) => {
  switch (payload.type) {
    case 'EVENT_WAS_SELECTED':
      return update(state, {$set: payload.event})
    case 'EVENT_WAS_DESELECTED':
      return update(state, {$set: null})
    default:
      return state
  }
}
