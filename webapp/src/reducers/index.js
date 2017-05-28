import { combineReducers } from 'redux';
import events from './events'
import currentEvent from './currentEvent'

const rootReducer = combineReducers({
    events,
    currentEvent
});
export default rootReducer;
