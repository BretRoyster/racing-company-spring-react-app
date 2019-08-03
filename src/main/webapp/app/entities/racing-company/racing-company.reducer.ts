import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRacingCompany, defaultValue } from 'app/shared/model/racing-company.model';

export const ACTION_TYPES = {
  SEARCH_RACINGCOMPANIES: 'racingCompany/SEARCH_RACINGCOMPANIES',
  FETCH_RACINGCOMPANY_LIST: 'racingCompany/FETCH_RACINGCOMPANY_LIST',
  FETCH_RACINGCOMPANY: 'racingCompany/FETCH_RACINGCOMPANY',
  CREATE_RACINGCOMPANY: 'racingCompany/CREATE_RACINGCOMPANY',
  UPDATE_RACINGCOMPANY: 'racingCompany/UPDATE_RACINGCOMPANY',
  DELETE_RACINGCOMPANY: 'racingCompany/DELETE_RACINGCOMPANY',
  RESET: 'racingCompany/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRacingCompany>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type RacingCompanyState = Readonly<typeof initialState>;

// Reducer

export default (state: RacingCompanyState = initialState, action): RacingCompanyState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RACINGCOMPANIES):
    case REQUEST(ACTION_TYPES.FETCH_RACINGCOMPANY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RACINGCOMPANY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RACINGCOMPANY):
    case REQUEST(ACTION_TYPES.UPDATE_RACINGCOMPANY):
    case REQUEST(ACTION_TYPES.DELETE_RACINGCOMPANY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RACINGCOMPANIES):
    case FAILURE(ACTION_TYPES.FETCH_RACINGCOMPANY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RACINGCOMPANY):
    case FAILURE(ACTION_TYPES.CREATE_RACINGCOMPANY):
    case FAILURE(ACTION_TYPES.UPDATE_RACINGCOMPANY):
    case FAILURE(ACTION_TYPES.DELETE_RACINGCOMPANY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RACINGCOMPANIES):
    case SUCCESS(ACTION_TYPES.FETCH_RACINGCOMPANY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_RACINGCOMPANY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RACINGCOMPANY):
    case SUCCESS(ACTION_TYPES.UPDATE_RACINGCOMPANY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RACINGCOMPANY):
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

const apiUrl = 'api/racing-companies';
const apiSearchUrl = 'api/_search/racing-companies';

// Actions

export const getSearchEntities: ICrudSearchAction<IRacingCompany> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RACINGCOMPANIES,
  payload: axios.get<IRacingCompany>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IRacingCompany> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RACINGCOMPANY_LIST,
    payload: axios.get<IRacingCompany>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRacingCompany> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RACINGCOMPANY,
    payload: axios.get<IRacingCompany>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRacingCompany> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RACINGCOMPANY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRacingCompany> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RACINGCOMPANY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRacingCompany> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RACINGCOMPANY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
