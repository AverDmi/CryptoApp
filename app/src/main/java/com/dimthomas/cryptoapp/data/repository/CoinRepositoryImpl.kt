package com.dimthomas.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dimthomas.cryptoapp.data.database.AppDatabase
import com.dimthomas.cryptoapp.data.mapper.CoinMapper
import com.dimthomas.cryptoapp.data.network.ApiFactory
import com.dimthomas.cryptoapp.domain.CoinInfo
import com.dimthomas.cryptoapp.domain.CoinRepository
import kotlinx.coroutines.delay

class CoinRepositoryImpl(
    private val application: Application
): CoinRepository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()
    private val apiService = ApiFactory.apiService

    private val mapper = CoinMapper()

    override fun getCoinInfoList(): LiveData<List<CoinInfo>>  = MediatorLiveData<List<CoinInfo>>().apply {
        addSource(coinInfoDao.getPriceList()) {
            value = it.map {
                mapper.mapDbModelToEntity(it)
            }
        }
    }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo> = MediatorLiveData<CoinInfo>().apply {
        addSource(coinInfoDao.getPriceInfoAboutCoin(fromSymbol)) {
            value = mapper.mapDbModelToEntity(it)
    }}

    override suspend fun loadData() {
        while (true) {
            try {
                val topCoins = apiService.getTopCoinsInfo(limit = 50)
                val fSyms = mapper.mapNamesListToString(topCoins)
                val jsonContainer = apiService.getFullPriceList(fSyms = fSyms)
                val coinInfoDtoList = mapper.mapJsonContainerToListCoiInfo(jsonContainer)
                val dbModelList = coinInfoDtoList.map { mapper.mapDtoToDbModel(it) }
                coinInfoDao.insertPriceList(dbModelList)
            } catch (e: Exception) {
            }
            delay(10000)
        }
    }
}