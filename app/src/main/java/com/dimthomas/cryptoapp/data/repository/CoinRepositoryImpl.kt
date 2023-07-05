package com.dimthomas.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.dimthomas.cryptoapp.data.database.AppDatabase
import com.dimthomas.cryptoapp.data.mapper.CoinMapper
import com.dimthomas.cryptoapp.data.network.ApiFactory
import com.dimthomas.cryptoapp.data.workers.RefreshDataWorker
import com.dimthomas.cryptoapp.domain.CoinInfo
import com.dimthomas.cryptoapp.domain.CoinRepository
import kotlinx.coroutines.delay

class CoinRepositoryImpl(
    private val application: Application
): CoinRepository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()
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

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            RefreshDataWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            RefreshDataWorker.makeRequest()
        )
    }
}