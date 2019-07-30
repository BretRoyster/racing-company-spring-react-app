import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITruckStop, defaultValue } from 'app/shared/model/truck-stop.model';

export const ACTION_TYPES = {
  SEARCH_TRUCKSTOPS: 'truckStop/SEARCH_TRUCKSTOPS',
  FETCH_TRUCKSTOP_LIST: 'truckStop/FETCH_TRUCKSTOP_LIST',
  FETCH_TRUCKSTOP: 'truckStop/FETCH_TRUCKSTOP',
  CREATE_TRUCKSTOP: 'truckStop/CREATE_TRUCKSTOP',
  UPDATE_TRUCKSTOP: 'truckStop/UPDATE_TRUCKSTOP',
  DELETE_TRUCKSTOP: 'truckStop/DELETE_TRUCKSTOP',
  RESET: 'truckStop/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITruckStop>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type TruckStopState = Readonly<typeof initialState>;

// Reducer

export default (state: TruckStopState = initialState, action): TruckStopState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_TRUCKSTOPS):
    case REQUEST(ACTION_TYPES.FETCH_TRUCKSTOP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TRUCKSTOP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TRUCKSTOP):
    case REQUEST(ACTION_TYPES.UPDATE_TRUCKSTOP):
    case REQUEST(ACTION_TYPES.DELETE_TRUCKSTOP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_TRUCKSTOPS):
    case FAILURE(ACTION_TYPES.FETCH_TRUCKSTOP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TRUCKSTOP):
    case FAILURE(ACTION_TYPES.CREATE_TRUCKSTOP):
    case FAILURE(ACTION_TYPES.UPDATE_TRUCKSTOP):
    case FAILURE(ACTION_TYPES.DELETE_TRUCKSTOP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_TRUCKSTOPS):
    case SUCCESS(ACTION_TYPES.FETCH_TRUCKSTOP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_TRUCKSTOP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TRUCKSTOP):
    case SUCCESS(ACTION_TYPES.UPDATE_TRUCKSTOP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TRUCKSTOP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/truck-stops';
const apiSearchUrl = 'api/_search/truck-stops';

// Actions

export const getSearchEntities: ICrudSearchAction<ITruckStop> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_TRUCKSTOPS,
  payload: axios.get<ITruckStop>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<ITruckStop> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TRUCKSTOP_LIST,
    payload: axios.get<ITruckStop>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ITruckStop> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TRUCKSTOP,
    payload: axios.get<ITruckStop>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITruckStop> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TRUCKSTOP,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITruckStop> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TRUCKSTOP,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITruckStop> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TRUCKSTOP,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
